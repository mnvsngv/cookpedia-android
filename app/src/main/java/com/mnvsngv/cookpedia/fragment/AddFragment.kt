package com.mnvsngv.cookpedia.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.fragment.adapter.AddRecipeIngredientsAdapter
import kotlinx.android.synthetic.main.fragment_add_recipe_ingredients.view.*


class AddFragment : Fragment(), AddRecipeIngredientsAdapter.Listener {

    private val ingredients: MutableList<RecipeIngredient> = ArrayList()
    private lateinit var listener: AddRecipeIngredientsListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recipe_ingredients, container, false)

        if (ingredients.size == 0) ingredients.add(RecipeIngredient("", ""))

        with(view.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = AddRecipeIngredientsAdapter(context,ingredients, this@AddFragment)
        }

        view.addIngredientsFab.setOnClickListener { listener.afterAddIngredients(ingredients) }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as AddRecipeIngredientsListener
    }

    override fun onAddIngredient() {
//        ingredients[ingredients.size-1] = getNewestIngredient()
        ingredients.add(RecipeIngredient("", ""))

        if (view?.list is RecyclerView) {
            with(view?.list) {
                if (this != null) {
                    adapter?.notifyDataSetChanged()
                    scrollToPosition(ingredients.size)
                }
            }
        }
    }


    // TODO Rename to Listener
    interface AddRecipeIngredientsListener {
        fun afterAddIngredients(ingredients: List<RecipeIngredient>)
    }

}
