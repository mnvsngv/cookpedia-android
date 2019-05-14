package com.mnvsngv.cookpedia.activity

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
        AddRecipeIngredientsFragment.AddRecipeIngredientsListener,
        AddRecipeStepsFragment.AddRecipeStepsListener,
        AddRecipePhotoFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        replaceWithFragment(AddRecipeIngredientsFragment())
    }

    override fun afterAddIngredients(ingredients: List<RecipeIngredient>) {
        replaceWithFragment(AddRecipeStepsFragment.newInstance(ingredients))
    }

    override fun afterAddSteps(recipe: RecipeItem) {
        replaceWithFragment(AddRecipePhotoFragment.newInstance(recipe))
    }

    override fun afterAddPhoto(uri: Uri) {

    }

    private fun replaceWithFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
