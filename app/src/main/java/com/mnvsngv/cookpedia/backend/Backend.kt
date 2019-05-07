package com.mnvsngv.cookpedia.backend

import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.User
import com.mnvsngv.cookpedia.dataclass.RecipeStep


interface Backend {
    fun registerUser(email: String, password: String, fullName: String, username: String)
    fun loginUser(email: String, password: String)
    fun addRecipe(name: String, steps: List<RecipeStep>)
    fun readAllRecipes(searchStr : String): MutableList<RecipeItem>
}