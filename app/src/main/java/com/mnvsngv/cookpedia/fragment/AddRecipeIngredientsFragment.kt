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


class AddRecipeIngredientsFragment : Fragment(), AddRecipeIngredientsAdapter.RecipeIngredientsListener {

    private val ingredients: MutableList<RecipeIngredient> = ArrayList()
    private lateinit var listener: AddRecipeIngredientsListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recipe_ingredients, container, false)

        onAddIngredient()

        with(view.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = AddRecipeIngredientsAdapter(ingredients, this@AddRecipeIngredientsFragment)
        }

        view.addIngredientsFab.setOnClickListener { listener.onNext() }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as AddRecipeIngredientsListener
    }

    override fun onAddIngredient() {
        ingredients.add(RecipeIngredient("", 1))

        if (view?.list is RecyclerView) {
            with(view?.list) {
                if(this != null) {
                    adapter?.notifyDataSetChanged()
                    scrollToPosition(ingredients.size)
                }
            }
        }
    }


    interface AddRecipeIngredientsListener {
        fun onNext()
    }

}
