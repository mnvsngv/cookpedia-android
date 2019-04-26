package com.mnvsngv.cookpedia.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    fun getAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun getDbInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    override fun updateUserDetails(userMap: HashMap<String, Any>, user_id: String?) {
        val userRef = getDbInstance().getReference("USER_DETAILS")

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

    override fun authenticateUser(email: String, password: String) {
        getAuthInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task->

            if(task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                val user = getAuthInstance().currentUser
                val user_id = user?.uid
                val userMap =  backendListener.getUserDetails(user_id)
                updateUserDetails(userMap, user_id)
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.displayRegistrationErr()
            }
        }
    }
}