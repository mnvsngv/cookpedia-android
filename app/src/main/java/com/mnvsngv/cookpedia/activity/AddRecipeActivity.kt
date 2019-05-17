package com.mnvsngv.cookpedia.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.AddRecipeIngredientsFragment
import com.mnvsngv.cookpedia.fragment.AddRecipePhotoFragment
import com.mnvsngv.cookpedia.fragment.AddRecipeStepsFragment


class AddRecipeActivity : AppCompatActivity(),
        AddRecipeIngredientsFragment.Listener,
        AddRecipeStepsFragment.AddRecipeStepsListener,
        AddRecipePhotoFragment.OnFragmentInteractionListener {

    private lateinit var recipe: RecipeItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, AddRecipeIngredientsFragment())
        transaction.commit()
    }

    override fun afterAddIngredients(ingredients: List<RecipeIngredient>) {
        replaceWithFragment(AddRecipeStepsFragment.newInstance(ingredients))
    }

    override fun afterAddSteps(recipe: RecipeItem) {
        this.recipe = recipe
        replaceWithFragment(AddRecipePhotoFragment.newInstance(recipe))
    }

    override fun afterAddPhoto(uri: Uri?) {
        recipe.image = uri.toString()

    }

    override fun afterRecipeUpload() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun replaceWithFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
