package com.example.flamvouritemap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flamvouritemap.firebase.FirebaseEvents
import com.google.firebase.auth.FirebaseUser


class RegisterViewModel(application: Application): AndroidViewModel(application) {
    private val firebaseEvents: FirebaseEvents=FirebaseEvents()

    fun registerUser(email:String, password:String):FirebaseUser?{

        return firebaseEvents.createUserPassEmail(email,password)
    }


}