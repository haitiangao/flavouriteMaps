package com.example.flamvouritemap.firebase


import android.widget.Toast
import com.example.flamvouritemap.model.FavouriteResult
import com.example.flamvouritemap.model.Geometry
import com.example.flamvouritemap.util.DebugLogger
import com.example.flamvouritemap.util.DebugLogger.logDebug
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.Observable
import java.util.*

class FirebaseEvents {

    private var username:String? = null
    private lateinit var favouriteReference: DatabaseReference
    //private val favouriteReference:DatabaseReference=userReference.child("Favourite/")
    val favourites: Observable<List<FavouriteResult>>
        get() = Observable.just(favouritePlaces)
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        setUserFavourite()
    }

    fun setEmail(username:String){
        this.username=username
        var strippedName = username.replace(".", "")
        favouriteReference= FirebaseDatabase.getInstance().reference.child(strippedName)
    }

    private fun setUserFavourite() {
        if(username!=null) {
            favouriteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    favouritePlaces.clear()
                    for (currentSnap in dataSnapshot.children) {
                        val currentFav = currentSnap.getValue(
                            FavouriteResult::class.java
                        )!!
                        favouritePlaces.add(currentFav)
                        logDebug("childname:$currentFav")
                    }
                    logDebug("Observable room2:  " + favouritePlaces.size)
                }

                override fun onCancelled(databaseError: DatabaseError) { //DebugLogger.logError(databaseError);
                }
            })
        }
    }

    fun sendNewFavourite(favouriteResult: FavouriteResult) {

        favouriteReference.child(favouriteResult.id).setValue(favouriteResult)

    }

    fun removeFavourite(favouriteResult: FavouriteResult) {

        favouriteReference.child(favouriteResult.id).removeValue()

    }

    fun getCurrentUser():FirebaseUser?{
        return auth.currentUser
    }

    fun createUserPassEmail(email:String, password:String): FirebaseUser? {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    DebugLogger.logDebug("Registering with email:" +email)
                    user = auth.currentUser
                    sendEmailVerification(user)

                } else {
                    // If sign in fails, display a message to the user.
                    DebugLogger.logError(task.exception!!)

                }
            }
        return user
    }


    fun loginUserPassEmail(email:String, password:String): FirebaseUser? {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    DebugLogger.logDebug("signInWithEmail:success: " +email)

                        user = auth.currentUser
                    DebugLogger.logDebug("current user: " +user)


                } else {
                    // If sign in fails, display a message to the user.
                    DebugLogger.logError(task.exception!!)

                }
            }
        DebugLogger.logDebug("current user: " +user)

        return user;
    }

     private fun sendEmailVerification(user:FirebaseUser?) {
         if (user != null) {
             user.email?.let { logDebug(it) }
         }
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    logDebug("Successfully sent to email")
                } else {
                    DebugLogger.logError(task.exception!!)
                }
            }
    }


    companion object {
        private var favouritePlaces: MutableList<FavouriteResult> = ArrayList()
        private var user: FirebaseUser? = null

    }



}