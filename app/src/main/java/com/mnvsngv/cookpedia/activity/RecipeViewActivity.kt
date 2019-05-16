package com.mnvsngv.cookpedia.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.adapter.RecipeListViewAdapter
import kotlinx.android.synthetic.main.activity_recipe_view.*
import com.mnvsngv.cookpedia.fragment.adapter.IngredientsListViewAdapter
import kotlinx.android.synthetic.main.dialog_ingredients_listview.view.*
import android.R.array
import android.content.DialogInterface
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.ArrayAdapter
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import kotlinx.android.synthetic.main.select_ingredient.*


class RecipeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mnvsngv.cookpedia.R.layout.activity_recipe_view)

        val recipe = intent.getParcelableExtra<RecipeItem>(RECIPE_KEY)
        recipeName.text = recipe.name
        Log.i("RecipeViewActivity", "Steps: ${recipe.steps.size}")
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = RecipeListViewAdapter(recipe.steps)

        viewIngredientsFab.setOnClickListener {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_ingredients_listview, null)

            val mBuilder = AlertDialog.Builder(this)

            var mListIngredients = mDialogView.findViewById<RecyclerView>(R.id.list)
            mListIngredients.layoutManager = LinearLayoutManager(this)
            mListIngredients.adapter = IngredientsListViewAdapter(recipe.ingredients)

            mBuilder.setView(mDialogView)

            var dialog = mBuilder.create()
            dialog.show()

        }
    }
}
