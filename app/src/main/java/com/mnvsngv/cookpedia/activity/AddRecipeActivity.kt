package com.mnvsngv.cookpedia.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.fragment.AddRecipeIngredientsFragment
import com.mnvsngv.cookpedia.fragment.AddRecipeStepsFragment

class AddRecipeActivity : AppCompatActivity(), AddRecipeIngredientsFragment.AddRecipeIngredientsListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        replaceWithFragment(AddRecipeIngredientsFragment())
    }

    override fun onNext() {
        replaceWithFragment(AddRecipeStepsFragment())
    }

    private fun replaceWithFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, newFragment)
        transaction.commit()
    }
}
