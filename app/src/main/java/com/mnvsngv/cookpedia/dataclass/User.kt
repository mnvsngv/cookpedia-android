package com.mnvsngv.cookpedia.dataclass

import java.util.ArrayList

data class User(val email: String, val username: String, val name: String, val user_recipes: ArrayList<Any> = arrayListOf())