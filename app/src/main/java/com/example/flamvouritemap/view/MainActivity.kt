package com.example.flamvouritemap.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.flamvouritemap.R
import com.example.flamvouritemap.util.DebugLogger
import com.example.flamvouritemap.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*


/*
Create an application that uses google places api to display all a user's favorite locations on a Map and also stroer favorite locations into a local Room DB.
The application should display places near. User must sign in to get to the home screen which displays a map and nearby places.
Application should;
- Have firebase Authentication(Email and Password)
- Have a Map fragment to display current location and all nearby places
- ability to save favorite places by clicking on them
- A second fragment displaying the list of all favorite places
- Ability to delete favorite places
- MVVM architecture
- Use RxJava observables
 */

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private var user: FirebaseUser? = null
    private lateinit var username:String
    private lateinit var passWord:String
    private lateinit var sharedPref:SharedPreferences
    private lateinit var editor:SharedPreferences.Editor
    private val REQUEST_CODE = 707



    @BindView(R.id.email_edittext)
    lateinit var emailEdit: EditText

    @BindView(R.id.password_edittext)
    lateinit var passwordEdit: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        sharedPref= getSharedPreferences("CurrentUser", Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        username= sharedPref.getString("Username","").toString()
        passWord= sharedPref.getString("Password","").toString()

        mainViewModel = ViewModelProvider(this).get<MainViewModel>(MainViewModel::class.java)

    }
    override fun onStart() {
        super.onStart()
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        }
        if (username!="" && passWord!=""){
            autoLogin()
        }

    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
                        requestPermissions()
                }
            }
        }

    private fun autoLogin(){
        mainViewModel.loginUser(username,passWord)
        mainViewModel.setEmail(username)
        user = mainViewModel.getCurrentUser()

        updateUI(user,username,passWord)
    }



    @OnClick(R.id.login_button)
    public fun loginClick(view: View)
    {
        val email: String = emailEdit.text.toString().trim { it <= ' ' }
        val passWord: String = passwordEdit.text.toString().trim { it <= ' ' }
        mainViewModel.loginUser(email,passWord)
        mainViewModel.setEmail(email)
        user = mainViewModel.getCurrentUser()

        updateUI(user,email,passWord)
    }


    @OnClick(R.id.new_user_button)
    fun registerClick() {
        startActivity(Intent(this,RegisterActivity::class.java))
    }


    private fun updateUI(user: FirebaseUser?,email:String,password:String) {
        if (user != null) {

            editor.putString("Username", email)
            editor.putString("Password", password)
            editor.commit()

            DebugLogger.logDebug("User:" + email)

            val intent = Intent(this,MapActivity::class.java).apply{
                putExtra("Username", email)
            }
            startActivity(intent)
        }
        else{
            Status.text = "User logged out."

        }

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
