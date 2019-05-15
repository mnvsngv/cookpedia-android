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
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import kotlinx.android.synthetic.main.recipe_griditem_home.view.*


class RecipeGridViewAdapter(
    private val context: Context?,
    private val recipeList: List<RecipeItem>,
    private val listener: RecipeGridViewAdapter.RecipeDisplayAdapterListener
) : RecyclerView.Adapter<RecipeGridViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_griditem_home, parent, false)
        Log.i("Adapter", "View type: $viewType")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe = recipeList.get(position)

        holder.recipeName.setText(recipe.name)
        context?.let {
            Glide.with(it).load(recipe.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.dish_default).override(50, 50).diskCacheStrategy(
                        DiskCacheStrategy.RESOURCE
                    )
                )
                .into(holder.recipeImage)
        }
        holder.itemView.setOnClickListener {
            listener.onRecipeClick(recipe)
        }

    }

    override fun getItemCount(): Int = recipeList.size

    interface RecipeDisplayAdapterListener {
        fun onRecipeClick(recipe: RecipeItem)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var recipeName: TextView = mView.recipeName
        var recipeImage: ImageView = mView.imageRecipe
    }
}
