package com.mnvsngv.cookpedia.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.fragment.adapter.SelectIngredientsAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.fragment_select_ingredients.view.*


class SelectIngredientsFragment : Fragment(), BackendListener, SelectIngredientsAdapter.Listener {

    private val backend = BackendFactory.getInstance(this)
    private val ingredients = ArrayList<RecipeIngredient>()
    private val selectedIngredients = HashSet<RecipeIngredient>()
    private lateinit var listener: Listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_ingredients, container, false)

        ingredients.clear()
        backend.getAllIngredients()

        view.list?.layoutManager = GridLayoutManager(this.context, 2)
        view.list?.adapter = SelectIngredientsAdapter(ingredients, this)

        view.searchRecipeFab.setOnClickListener { listener.onIngredientsSelected(selectedIngredients.toList()) }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as Listener
    }

    override fun onGetAllIngredients(ingredients: List<RecipeIngredient>) {
        this.ingredients.addAll(ingredients)
        view?.list?.adapter?.notifyDataSetChanged()
    }

    override fun onIngredientChecked(ingredient: RecipeIngredient, selected: Boolean) {
        Log.i("tagger", ingredient.name)
        if (selected) selectedIngredients.add(ingredient)
        else selectedIngredients.remove(ingredient)
    }

    interface Listener {
        fun onIngredientsSelected(ingredients: List<RecipeIngredient>)
    }

}
