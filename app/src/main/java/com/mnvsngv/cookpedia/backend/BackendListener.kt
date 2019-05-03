package com.mnvsngv.cookpedia.backend

interface BackendListener {
    fun onRegistrationFailure() {}
    fun onLoginSuccess() {}
    fun onRegisterSuccess() {}
}