package com.mnvsngv.cookpedia.backend

interface Backend {

    fun updateUserDetails(userMap: HashMap<String, Any>, user_id:String?)
    fun registerUser(email: String, password: String)
    fun loginUser(email: String, password: String)
}