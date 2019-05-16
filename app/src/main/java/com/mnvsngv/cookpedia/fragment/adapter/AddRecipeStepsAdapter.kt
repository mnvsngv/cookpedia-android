package com.mnvsngv.cookpedia.fragment.adapter


import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.dataclass.RecipeStep
import com.mnvsngv.cookpedia.fragment.adapter.listener.TextChangedListener
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
            holder.mAddStepButton?.setOnClickListener {
                if (mValues[position-1].description.isNotEmpty()) {
                    listener.onAddStep()
                }
            }
        } else {
            holder.stepChangedListener.updatePosition(position)
            val item = mValues[position]
            holder.mIdView?.text = item.stepNumber.toString()
            holder.mContentView?.text = item.description

            holder.mAddPhotoButton?.setOnClickListener { listener.onAddPhotoFor(item) }

            val imageUri = Uri.parse(item.imageSource)
            if (imageUri != Uri.EMPTY) {
                // TODO Why doesn't this work when using Gallery?
                holder.mAddPhotoButton?.let {
                    Glide.with(it.context).load(imageUri).into(it)
                }
            } else {
                holder.mAddPhotoButton?.setImageResource(R.drawable.ic_add_photo_dark)
            }

            if (position == mValues.size - 1) {
                holder.mContentView?.requestFocus()
            }

            with(holder.mView) {
                tag = item
            }
        }

    }

    override fun getItemCount(): Int = mValues.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == mValues.size) R.layout.add_step_button else R.layout.fragment_add_step
    }

    // TODO Rename variables
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView? = mView.item_number
        val mContentView: TextView? = mView.content
        val mAddStepButton: ImageButton? = mView.addStepButton
        val mAddPhotoButton: ImageView? = mView.addPhoto
        val stepChangedListener = TextChangedListener { position, s -> mValues[position].description = s }

        init {
            mContentView?.addTextChangedListener(stepChangedListener)
        }

        override fun toString(): String {
            return super.toString() + " '" + mContentView?.text + "'"
        }
    }

    // TODO Rename to Listener
    interface RecipeStepAdapterListener {
        fun onAddStep()
        fun onAddPhotoFor(step: RecipeStep)
    }
}
