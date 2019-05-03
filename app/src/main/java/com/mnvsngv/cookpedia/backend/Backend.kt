package com.mnvsngv.cookpedia.backend

import com.mnvsngv.cookpedia.dataclass.User


interface Backend {
    fun updateUserDetails(user: User, user_id:String)
    fun registerUser(email: String, password: String, fullName: String, username: String)
    fun loginUser(email: String, password: String)
}