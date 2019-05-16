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


class AddRecipeIngredientsFragment : Fragment(), AddRecipeIngredientsAdapter.RecipeIngredientsListener
     {
    private val ingredients: MutableList<RecipeIngredient> = ArrayList()
    private lateinit var listener: AddRecipeIngredientsListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recipe_ingredients, container, false)

        if (ingredients.size == 0) onAddIngredient()

        with(view.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = AddRecipeIngredientsAdapter(context,ingredients, this@AddRecipeIngredientsFragment)
        }

        view.addIngredientsFab.setOnClickListener { listener.afterAddIngredients(getIngredients()) }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as AddRecipeIngredientsListener
    }

    override fun onAddIngredient() {
        val cachedIngredients = getIngredients()
        ingredients.removeAll { true }
        ingredients.addAll(cachedIngredients)
        ingredients.add(RecipeIngredient("", "", ""))

        if (view?.list is RecyclerView) {
            with(view?.list) {
                if (this != null) {
                    adapter?.notifyDataSetChanged()
                    scrollToPosition(ingredients.size)
                }
            }
        }
    }

    private fun getIngredients(): List<RecipeIngredient> {
        if (view?.list is RecyclerView) {
            with(view?.list as RecyclerView) {

                val finalIngredients = ArrayList<RecipeIngredient>()
                for (i in 0 until (adapter as AddRecipeIngredientsAdapter).itemCount - 1) {
                    val nextIngredient = getChildViewHolder(getChildAt(i))
                    if (nextIngredient is AddRecipeIngredientsAdapter.ViewHolder) {
                        val content = nextIngredient.mContentView?.text.toString()
                        val quantity = nextIngredient.mQuantityView?.text.toString()
                        val serving = nextIngredient.mServingView?.selectedItem.toString()

                        if (content.isNotEmpty()) {
                            finalIngredients.add(RecipeIngredient(content, quantity, serving))
                        }
                    }
                }
                return finalIngredients
            }
        }
        return listOf()

    }


    interface AddRecipeIngredientsListener {
        fun afterAddIngredients(ingredients: List<RecipeIngredient>)
    }

}
