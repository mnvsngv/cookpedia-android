package com.mnvsngv.cookpedia.fragment.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.fragment.adapter.listener.TextChangedListener
import kotlinx.android.synthetic.main.add_ingredient_button.view.*
import kotlinx.android.synthetic.main.fragment_add_ingredient.view.*
import kotlinx.android.synthetic.main.fragment_add_step.view.content


class AddRecipeIngredientsAdapter(
    private val mContext: Context,
    private val mValues: MutableList<RecipeIngredient>,
    private val listener: Listener
) : RecyclerView.Adapter<AddRecipeIngredientsAdapter.ViewHolder>() {

    private val servingItems = mContext.resources.getStringArray(R.array.spinnerItems)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == mValues.size) {
            holder.mButton?.setOnClickListener {
                if (validateIngredient(mValues[position-1])) {
                    listener.onAddIngredient()
                } else {
                    Toast.makeText(mContext, R.string.invalid_ingredient, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            holder.nameChangedListener.updatePosition(position)
            holder.quantityChangedListener.updatePosition(position)
            holder.servingChangedListener.updatePosition(position)

            val ingredient = mValues[position]
            holder.mContentView?.setText(ingredient.name, TextView.BufferType.EDITABLE)
            holder.mQuantityView?.text = ingredient.quantity
            holder.mServingView?.setSelection(indexOfSpinnerElement(ingredient.serving))

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

    private fun validateIngredient(ingredient: RecipeIngredient): Boolean {
        return (ingredient.name.isNotEmpty() && ingredient.quantity.isNotEmpty())
    }

    private fun indexOfSpinnerElement(element: String): Int {
        for (i in 0 until servingItems.size) {
            if (servingItems[i] == element) return i
        }
        return 0
    }

    interface Listener {
        fun onAddIngredient()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: EditText? = mView.content
        val mQuantityView: TextView? = mView.quantity
        val mServingView: Spinner? = mView.serving
        val mButton: ImageButton? = mView.addIngredientButton
        val nameChangedListener = TextChangedListener { position, s -> mValues[position].name = s }
        val quantityChangedListener = TextChangedListener { position, s -> mValues[position].quantity = s }
        val servingChangedListener = SelectionListener { ingredientPosition, spinnerPosition ->
            mValues[ingredientPosition].serving = servingItems[spinnerPosition]
        }

        init {
            mContentView?.addTextChangedListener(nameChangedListener)
            mQuantityView?.addTextChangedListener(quantityChangedListener)
            mServingView?.onItemSelectedListener = servingChangedListener
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView?.text + "'"
        }

    }

    inner class SelectionListener(private val run: (ingredientPosition: Int, spinnerPosition: Int) -> Unit) : AdapterView.OnItemSelectedListener {

        private var ingredientPosition = -1

        fun updatePosition(newPosition: Int) {
            ingredientPosition = newPosition
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            run(ingredientPosition, position)
        }
    }
}
