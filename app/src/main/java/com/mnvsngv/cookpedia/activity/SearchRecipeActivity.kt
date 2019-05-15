package com.mnvsngv.cookpedia.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.RecipeListFragment
import com.mnvsngv.cookpedia.fragment.SelectIngredientsFragment
import com.mnvsngv.cookpedia.singleton.BackendFactory

class SearchRecipeActivity : AppCompatActivity(), SelectIngredientsFragment.Listener, BackendListener {

    private val backend = BackendFactory.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_recipe)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.searchFragmentContainer, SelectIngredientsFragment())
        transaction.commit()
    }

    override fun onIngredientsSelected(ingredients: List<RecipeIngredient>) {
        backend.searchRecipesUsing(ingredients)
    }

    override fun onSearchRecipesUsing(recipes: List<RecipeItem>) {
        replaceFragmentWith(RecipeListFragment.newInstance(recipes))
    }

    private fun replaceFragmentWith(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.searchFragmentContainer, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
