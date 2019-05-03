package com.mnvsngv.cookpedia.backend

interface BackendListener {
    fun onLoginSuccess() {}
    fun onRegisterSuccess() {}
    fun onRegisterFailure() {}
}