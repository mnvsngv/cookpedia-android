package com.mnvsngv.cookpedia.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import kotlinx.android.synthetic.main.activity_recipe_view.*


class RecipeViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mnvsngv.cookpedia.R.layout.activity_recipe_view)

        recipeName.text = intent.getParcelableExtra<RecipeItem>("HOOYOO").name
    }
}
