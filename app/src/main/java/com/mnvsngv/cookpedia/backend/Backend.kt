package com.mnvsngv.cookpedia.backend


interface Backend {
    fun registerUser(email: String, password: String, fullName: String, username: String)
    fun loginUser(email: String, password: String)
}