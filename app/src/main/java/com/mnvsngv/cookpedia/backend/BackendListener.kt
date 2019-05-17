package com.mnvsngv.cookpedia.backend

import android.net.Uri
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem


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