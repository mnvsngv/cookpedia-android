package com.mnvsngv.cookpedia.backend

interface BackendListener {
    fun loadCookpediaHome() {}
    fun displayRegistrationErr() {}
    fun onLoginSuccess() {}
    fun onRegisterSuccess() {}
    fun notifyChange() {}
}