package com.example.flamvouritemap.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.flamvouritemap.R
import com.example.flamvouritemap.viewmodel.MainViewModel
import com.example.flamvouritemap.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseUser

class RegisterFragment : Fragment() {

    private lateinit var registerViewModel:RegisterViewModel
    @BindView(R.id.register_email_edit)
    lateinit var registerUserName: EditText
    @BindView(R.id.register_password_edit)
    lateinit var registerPassword: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view)
        registerViewModel= ViewModelProvider(this).get<RegisterViewModel>(RegisterViewModel::class.java)

    }



    @OnClick(R.id.register_button)
    fun registerUser(view: View) {

        val email = registerUserName.text.toString().trim { it <= ' ' }
        val passWord = registerPassword.text.toString().trim { it <= ' ' }
        registerViewModel.registerUser(email,passWord)

        (activity as RegisterActivity).registerComplete()
    }


}