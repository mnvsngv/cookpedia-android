package com.mnvsngv.cookpedia.fragment.adapter


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.fragment.adapter.listener.TextChangedListener
import kotlinx.android.synthetic.main.add_ingredient_button.view.*
import kotlinx.android.synthetic.main.fragment_add_ingredient.view.*
import kotlinx.android.synthetic.main.fragment_add_step.view.content


class AddRecipeIngredientsAdapter(
    private val mValues: MutableList<RecipeIngredient>,
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
                if (mValues[position-1].name.isNotEmpty()) {
                    listener.onAddIngredient()
                }
            }
        } else {
            holder.nameChangedListener.updatePosition(position)
            holder.quantityChangedListener.updatePosition(position)
            val ingredient = mValues[position]
            holder.mContentView?.setText(ingredient.name, TextView.BufferType.EDITABLE)
            holder.mQuantityView?.text = ingredient.quantity

            if (position == mValues.size - 1) {
                holder.mContentView?.requestFocus()
            }

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

    // TODO Rename to Listener
    interface RecipeIngredientsListener {
        fun onAddIngredient()
    }

    // TODO Rename variables
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: EditText? = mView.content
        val mQuantityView: TextView? = mView.quantity
        val mButton: ImageButton? = mView.addIngredientButton
        val nameChangedListener = TextChangedListener { position, s -> mValues[position].name = s }
        val quantityChangedListener = TextChangedListener { position, s -> mValues[position].quantity = s }

        init {
            mContentView?.addTextChangedListener(nameChangedListener)
            mQuantityView?.addTextChangedListener(quantityChangedListener)
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView?.text + "'"
        }

    }
}
