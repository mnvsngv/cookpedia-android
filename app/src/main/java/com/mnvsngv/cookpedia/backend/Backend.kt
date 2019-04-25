package com.mnvsngv.cookpedia.backend

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface Backend {

    fun getAuthInstance(): FirebaseAuth
    fun getDbInstance(): FirebaseDatabase
}