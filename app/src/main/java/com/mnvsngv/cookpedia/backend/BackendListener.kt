package com.mnvsngv.cookpedia.backend

interface BackendListener {
    fun getUserDetails(user_id: String) {}
    fun loadCookpediaHome() {}
    fun displayRegistrationErr() {}
    fun onLoginSuccess() {}
    fun onRegisterSuccess() {}
}