package com.mnvsngv.cookpedia.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    override fun updateUserDetails(userMap: HashMap<String, Any>, user_id: String?) {
        val userRef = db.getReference("USER_DETAILS")

        val childUpdates = HashMap<String, Any>()
        user_id?.let { childUpdates[it] = userMap }

        userRef.updateChildren(childUpdates).addOnCompleteListener {task ->

            if (task.isSuccessful) {
                // Once the user is successfully registered, the InstaPost activity is launched where the registered user can
                // view all the posts in the Instapost application uploaded by every user
                backendListener.loadCookpediaHome()
            } else {
                backendListener.displayRegistrationErr()
            }
        }
    }

    override fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            if(task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                backendListener.onLoginSuccess()
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.displayRegistrationErr()
            }
        }
    }

    override fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task->

            if(task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                val user = auth.currentUser
                val user_id = user?.uid
                backendListener.getUserDetails(user_id)
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.displayRegistrationErr()
            }
        }
    }
}