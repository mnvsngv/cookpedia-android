package com.mnvsngv.cookpedia.backend

import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem


// TODO Rename methods so they more closely correspond to Backend functions
interface BackendListener {
    fun onLoginSuccess() {}
    fun onRegisterSuccess() {}
    fun onRegisterFailure() {}
    fun onLoginFailure() {}
    fun onRecipeUploadSuccess() {}
    fun onReadAllRecipes(recipes: List<RecipeItem>) {}
    fun onGetAllIngredients(ingredients: List<RecipeIngredient>) {}
    fun onSearchRecipesUsing(recipes: List<RecipeItem>) {}
}