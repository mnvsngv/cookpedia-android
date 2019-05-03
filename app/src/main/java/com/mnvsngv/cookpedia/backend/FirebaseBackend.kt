package com.mnvsngv.cookpedia.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val USERS_COLLECTION = "Users"

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            if(task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                backendListener.onLoginSuccess()
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.onRegistrationFailure()
            }
        }
    }

    override fun registerUser(
        email: String,
        password: String,
        fullName: String,
        username: String
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task->

            if(task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
//                val user = auth.currentUser
//                val user_id = user?.uid
                backendListener.onRegisterSuccess()
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.onRegistrationFailure()
            }
        }
    }
}