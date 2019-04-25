package com.mnvsngv.cookpedia.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mnvsngv.cookpedia.DataClass.UserItem

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    fun getAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun getDbInstance(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    override fun updateUserDetails(userMap: HashMap<String, Any>, user_id:String) {
        val userRef = getDbInstance().getReference("USER_DETAILS")

        val childUpdates = HashMap<String, Any>()
        childUpdates[user_id] = userMap

        userRef.updateChildren()
    }

    fun authenticateUser(email: String, password: String) {
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
                updateUserDetails(null)
            }
        }
    }

    fun addUser(map: HashMap<String, Any>) {

    }
}