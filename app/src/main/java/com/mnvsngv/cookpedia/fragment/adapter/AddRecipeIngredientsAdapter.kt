package com.mnvsngv.cookpedia.fragment.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import kotlinx.android.synthetic.main.add_ingredient_button.view.*
import kotlinx.android.synthetic.main.fragment_add_ingredient.view.*
import kotlinx.android.synthetic.main.fragment_add_step.view.content


class AddRecipeIngredientsAdapter(
    private val mValues: List<RecipeIngredient>,
    private val listener: RecipeIngredientsListener
) : RecyclerView.Adapter<AddRecipeIngredientsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == mValues.size) {
            holder.mButton?.setOnClickListener {
                listener.onAddIngredient()
            }
        } else {
            val ingredient = mValues[position]
            holder.mContentView?.text = ingredient.name
            holder.mQuantityView?.text = ingredient.quantity.toString()

            with(holder.mView) {
                tag = ingredient
            }

        }
    }

    override fun getItemCount(): Int = mValues.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == mValues.size) R.layout.add_ingredient_button
        else R.layout.fragment_add_ingredient
    }

    interface RecipeIngredientsListener {
        fun onAddIngredient()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView? = mView.content
        val mQuantityView: TextView? = mView.quantity
        val mButton: ImageButton? = mView.addIngredientButton

        override fun toString(): String {
            return super.toString() + " '" + mContentView?.text + "'"
        }

    }
}
