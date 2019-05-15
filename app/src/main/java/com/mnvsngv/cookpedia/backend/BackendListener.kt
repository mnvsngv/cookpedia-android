package com.mnvsngv.cookpedia.backend

interface BackendListener {
    fun onLoginSuccess() {}
    fun onRegisterSuccess() {}
    fun notifyChange() {}
    fun onRegisterFailure() {}
    fun onLoginFailure() {}
    fun onRecipeUploadSuccess() {}
}