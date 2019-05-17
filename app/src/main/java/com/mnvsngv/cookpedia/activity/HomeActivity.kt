package com.mnvsngv.cookpedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.INGREDIENTS_KEY
import com.mnvsngv.cookpedia.fragment.adapter.RecipeGridViewAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.app_bar_home.*
import java.util.ArrayList


private const val ADD_RECIPE = 1
const val RECIPE_KEY = "recipe"

// TODO Rename XML variables
// TODO Remove dead code & layout files
// TODO Rename layout files and Kotlin files for naming consistency

// TODO Code cleanup
class HomeActivity : AppCompatActivity(), BackendListener, RecipeGridViewAdapter.RecipeDisplayAdapterListener {


    // TODO use list instead of mutable list if possible
    var recipes: MutableList<RecipeItem> = mutableListOf()
    var ingredients: MutableList<RecipeIngredient> = mutableListOf()
    private val backend = BackendFactory.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        var mFragView = inflater.inflate(R.layout.fragment_recipe_grid, container, false)
//        recipe_recycler_view = findViewById(R.id.recipe_grid_view)
        list?.layoutManager = GridLayoutManager(this,2)
        list?.adapter = RecipeGridViewAdapter(this, recipes, this)
//        recipeAdapter = list?.adapter as RecipeGridViewAdapter

        backend.readUserRecipes()

        addRecipeFab.setOnClickListener {
            startActivityForResult(Intent(this, AddRecipeActivity::class.java), ADD_RECIPE)
        }

        searchRecipeFab.setOnClickListener {
            startActivity(Intent(this, SearchRecipeActivity::class.java))
        }
    }

    override fun onReadAllRecipes(recipes: List<RecipeItem>) {
        Log.i("tagger", "onReadAllRecipes")
        this.recipes.clear()
        this.recipes.addAll(recipes)
        list?.adapter?.notifyDataSetChanged()
    }

    override fun onRecipeClick(recipe: RecipeItem) {
        val intent = Intent(this, RecipeViewActivity::class.java)
        intent.putExtra(RECIPE_KEY, recipe)
        startActivity(intent)
    }

//    override fun onGetAllIngredients(ingredients: List<RecipeIngredient>) {
//        this.ingredients.clear()
//        this.ingredients.addAll(ingredients)
//        intent.(INGREDIENTS_KEY,ingredients)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_RECIPE -> {
                    backend.readUserRecipes()
                }
            }
        }
    }

}
