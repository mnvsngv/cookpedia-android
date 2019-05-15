package com.mnvsngv.cookpedia.backend

import com.google.firebase.firestore.DocumentReference
import com.mnvsngv.cookpedia.dataclass.RecipeItem


interface Backend {
    fun registerUser(email: String, password: String, fullName: String, username: String)
    fun loginUser(email: String, password: String)
    fun addRecipe(recipe: RecipeItem)
    fun readAllRecipes(): MutableList<RecipeItem>
    fun updateUserRecipes(recipe: RecipeItem)
    fun readUserRecipes() : MutableList<RecipeItem>
}