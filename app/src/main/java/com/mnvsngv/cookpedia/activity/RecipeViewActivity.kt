package com.mnvsngv.cookpedia.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.adapter.IngredientsListViewAdapter
import com.mnvsngv.cookpedia.fragment.adapter.RecipeListViewAdapter
import kotlinx.android.synthetic.main.activity_recipe_view.*

class RecipeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_view)

        val recipe = intent.getParcelableExtra<RecipeItem>(RECIPE_KEY)
        recipeName.text = recipe.name
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = RecipeListViewAdapter(this,recipe.steps)

        viewIngredientsFab.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_ingredients_listview, null, false)
            val dialogBuilder = AlertDialog.Builder(this)

            val listIngredients = dialogView.findViewById<RecyclerView>(R.id.dialogList)
            listIngredients.layoutManager = LinearLayoutManager(this)
            listIngredients.adapter = IngredientsListViewAdapter(recipe.ingredients)

            dialogBuilder.setView(dialogView)
            dialogBuilder.create().show()
        }
    }
}
