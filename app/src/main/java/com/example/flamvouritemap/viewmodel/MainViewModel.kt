package com.example.flamvouritemap.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.flamvouritemap.firebase.FirebaseEvents
import com.google.firebase.auth.FirebaseUser


class MainViewModel(application: Application): AndroidViewModel(application) {
    private val firebaseEvents: FirebaseEvents = FirebaseEvents()

    fun setEmail(username:String){
        firebaseEvents.setEmail(username)
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseEvents.getCurrentUser()
    }

    fun loginUser(email:String, password:String):FirebaseUser?{
        return firebaseEvents.loginUserPassEmail(email,password)

    }


}