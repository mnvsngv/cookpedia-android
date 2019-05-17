package com.mnvsngv.cookpedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.adapter.RecipeGridViewAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.app_bar_home.*


private const val ADD_RECIPE = 1
const val RECIPE_KEY = "recipe"

// TODO Rename XML variables
// TODO Remove dead code & layout files
// TODO Rename layout files and Kotlin files for naming consistency

class HomeActivity : AppCompatActivity(), BackendListener, RecipeGridViewAdapter.Listener {

    private val recipes = mutableListOf<RecipeItem>()
    private val backend = BackendFactory.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        list?.layoutManager = GridLayoutManager(this,2)
        list?.adapter = RecipeGridViewAdapter(this, recipes, this)

        progressBar.visibility = View.VISIBLE
        backend.readUserRecipes()

        addRecipeFab.setOnClickListener {
            startActivityForResult(Intent(this, AddRecipeActivity::class.java), ADD_RECIPE)
        }

        searchRecipeFab.setOnClickListener {
            startActivity(Intent(this, SearchRecipeActivity::class.java))
        }
    }

    override fun onReadAllRecipes(recipes: List<RecipeItem>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        list?.adapter?.notifyDataSetChanged()
        progressBar.visibility = View.INVISIBLE
    }

    override fun onRecipeClick(recipe: RecipeItem) {
        val intent = Intent(this, RecipeViewActivity::class.java)
        intent.putExtra(RECIPE_KEY, recipe)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_RECIPE -> {
                    backend.readAllRecipes()
                }
            }
        }
    }

}
