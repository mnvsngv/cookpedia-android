package com.mnvsngv.cookpedia.fragment.adapter


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import kotlinx.android.synthetic.main.ingredients_list_view.view.*


class IngredientsListViewAdapter(
    private val mValues: List<RecipeIngredient>
) : RecyclerView.Adapter<IngredientsListViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredients_list_view, parent, false)

        Log.i("Adapter", "View type: $viewType")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mValues[position]
        holder.mIngredientName?.text = item.name
        holder.mIngredientAmount?.text = ", " + item.quantity + " " + item.serving
        Log.i("tagger", item.toString())
        with(holder.mView) {
            tag = item
        }

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
            val mIngredientName = mView.ingName
            val mIngredientAmount = mView.ingAmount

//        override fun toString(): String {
//            return super.toString() + " '" + mIngredientName?.text + "'"
//        }
    }
}
