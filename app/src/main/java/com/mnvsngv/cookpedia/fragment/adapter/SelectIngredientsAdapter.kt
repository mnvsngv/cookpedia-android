package com.mnvsngv.cookpedia.fragment.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import kotlinx.android.synthetic.main.select_ingredient.view.*

//Adapter class to display the recycler view
class SelectIngredientsAdapter(
    private val ingredients: List<RecipeIngredient>,
    private val listener: Listener
) : RecyclerView.Adapter<SelectIngredientsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.select_ingredient, parent, false)

        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val ingredient = ingredients.get(position)

        holder.ingredientSelected.text = ingredient.name
        holder.ingredientSelected.setOnCheckedChangeListener { _, isChecked ->
            listener.onIngredientChecked(ingredient, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    interface Listener {
        fun onIngredientChecked(ingredient: RecipeIngredient, selected: Boolean)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ingredientSelected: CheckBox = view.ingredientSelected
    }
}