package com.mnvsngv.cookpedia.fragment.adapter


import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeStep
import kotlinx.android.synthetic.main.add_step_button.view.*
import kotlinx.android.synthetic.main.fragment_add_step.view.*


class AddRecipeStepsAdapter(
    private val mValues: List<RecipeStep>,
    private val listener: RecipeStepAdapterListener
) : RecyclerView.Adapter<AddRecipeStepsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)

        Log.i("Adapter", "View type: $viewType")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position == mValues.size) {
            holder.mButton?.setOnClickListener {
                listener.onAddStep()
            }
        } else {
            val item = mValues[position]
            holder.mIdView?.text = item.stepNumber.toString()
            holder.mContentView?.text = item.description

            with(holder.mView) {
                tag = item
            }
        }

    }

    override fun getItemCount(): Int = mValues.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == mValues.size) R.layout.add_step_button else R.layout.fragment_add_step
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView? = mView.item_number
        val mContentView: TextView? = mView.content
        val mButton: ImageButton? = mView.addStepButton

        override fun toString(): String {
            return super.toString() + " '" + mContentView?.text + "'"
        }
    }

    interface RecipeStepAdapterListener {
        fun onAddStep()
    }
}
