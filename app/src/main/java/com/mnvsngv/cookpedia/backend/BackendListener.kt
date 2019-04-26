package com.mnvsngv.cookpedia.backend

import com.mnvsngv.cookpedia.DataClass.UserItem

interface BackendListener {
    fun getUserDetails(user_id: String?): HashMap<String, Any>
    fun loadCookpediaHome()
    fun displayRegistrationErr()
}