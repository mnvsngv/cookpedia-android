package com.mnvsngv.cookpedia.fragment.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeStep
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.recipe_step_view.view.*


class RecipeListViewAdapter(
    private val mContext: Context,
    private val mValues: List<RecipeStep>
) : RecyclerView.Adapter<RecipeListViewAdapter.ViewHolder>(), BackendListener {

    val backend = BackendFactory.getInstance(this)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_step_view, parent, false)

        Log.i("Adapter", "View type: $viewType")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mValues[position]
        holder.mIdView?.text = item.stepNumber.toString()
        holder.mContentView?.text = item.description

        if (item.imageSource.isNotEmpty()) {
            backend.loadImageFrom(item.imageSource) { uri ->
                holder.mStepImage?.let { Glide.with(mContext).load(uri).into(it) }
            }
        }
        with(holder.mView) {
            tag = item
        }

    }

    override fun getItemCount(): Int = mValues.size

    // TODO Rename variables
    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView? = mView.stepNumber
        val mContentView: TextView? = mView.stepDescription
        val mStepImage: ImageView? = mView.stepImage
        override fun toString(): String {
            return super.toString() + " '" + mContentView?.text + "'"
        }
    }
}
