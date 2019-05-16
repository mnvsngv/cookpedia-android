package com.mnvsngv.cookpedia.fragment.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import kotlinx.android.synthetic.main.add_ingredient_button.view.*
import kotlinx.android.synthetic.main.fragment_add_ingredient.view.*
import kotlinx.android.synthetic.main.fragment_add_step.view.content
import android.widget.AdapterView
import android.widget.TextView




class AddRecipeIngredientsAdapter(
    private val mContext: Context,
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
            holder.mQuantityView?.text = ingredient.quantity
            holder.mServingView?.setSelection(indexOfSpinnerElement(ingredient.serving))

//            ArrayAdapter.createFromResource(
//                mContext,
//                R.array.spinnerItems,
//                android.R.layout.simple_spinner_item
//            ).also { adapter ->
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                // Apply the adapter to the spinner
//                holder.mServingView?.adapter = adapter
//            }
//
//            holder.mServingView?.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    adapter: AdapterView<*>, v: View,
//                    position: Int, id: Long
//                ) {
//
//                    // get selected serving
//                    val mServing = adapter.getItemAtPosition(position).toString()
//
//                    adapter.setSelection(position)
//                }
//
//                override fun onNothingSelected(arg0: AdapterView<*>) {
//                    // TODO Auto-generated method stub
//                }
//            })


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

    private fun indexOfSpinnerElement(element: String): Int {
        val items = mContext.resources.getStringArray(R.array.spinnerItems)
        for (i in 0 until items.size) {
            if (items[i] == element) return i
        }
        return 0
    }

    interface RecipeIngredientsListener {
        fun onAddIngredient()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView: TextView? = mView.content
        val mQuantityView: TextView? = mView.quantity
        val mServingView:Spinner? = mView.serving
        val mButton: ImageButton? = mView.addIngredientButton


        override fun toString(): String {
            return super.toString() + " '" + mContentView?.text + "'"
        }

    }
}
