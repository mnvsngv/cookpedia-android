package com.mnvsngv.cookpedia.dataclass

import java.util.*

// TODO Code cleanup
data class User(val email: String, val username: String, val name: String, val user_recipes: ArrayList<Any> = arrayListOf())