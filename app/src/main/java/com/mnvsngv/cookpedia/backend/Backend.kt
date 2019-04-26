package com.mnvsngv.cookpedia.backend

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface Backend {

    fun updateUserDetails(userMap: HashMap<String, Any>, user_id:String?)

    fun authenticateUser(email: String, password: String)
}