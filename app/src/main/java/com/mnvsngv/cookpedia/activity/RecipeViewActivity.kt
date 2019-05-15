package com.mnvsngv.cookpedia.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.adapter.RecipeListViewAdapter
import kotlinx.android.synthetic.main.activity_recipe_view.*


class RecipeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mnvsngv.cookpedia.R.layout.activity_recipe_view)

        val recipe = intent.getParcelableExtra<RecipeItem>("HOOYOO")
        recipeName.text = recipe.name
        Log.i("RecipeViewActivity", "Steps: ${recipe.steps.size}")
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = RecipeListViewAdapter(recipe.steps)
    }
}
