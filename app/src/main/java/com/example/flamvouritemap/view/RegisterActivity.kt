package com.example.flamvouritemap.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.flamvouritemap.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var  registerFragment:RegisterFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerFragment= RegisterFragment()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_holder, registerFragment)

            .commit()
    }

    fun registerComplete(){

        finish()
    }


    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}
