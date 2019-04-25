package com.mnvsngv.cookpedia.backend

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    override fun getAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun getDbInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    fun getUserRef(): DatabaseReference {
        return getDbInstance().getReference("USER_DETAILS")
    }

    fun authenticateUser(email: String, password: String) {
        getAuthInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task->

            if(task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                val user = getAuthInstance().currentUser
                val user_id = user?.uid
                backendListener.updateUserDetails(user_id)
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.updateUserDetails(null)
            }
        }
    }

    fun addUser(map: HashMap<String, Any>) {

    }
}