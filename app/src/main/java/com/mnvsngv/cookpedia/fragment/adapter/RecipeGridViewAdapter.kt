package com.mnvsngv.cookpedia.fragment.adapter


import android.content.Context
import android.net.Uri
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.recipe_griditem_home.view.*


class RecipeGridViewAdapter(
    private val context: Context?,
    private val recipeList: List<RecipeItem>,
    private val listener: Listener
) : RecyclerView.Adapter<RecipeGridViewAdapter.ViewHolder>(), BackendListener {

    val backend = BackendFactory.getInstance(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_griditem_home, parent, false)
        Log.i("Adapter", "View type: $viewType")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe = recipeList.get(position)

        holder.recipeName.setText(recipe.name)

        if (recipe.image.isNotEmpty()) {
            backend.loadImageFrom(recipe.image) {
                Glide.with(holder.recipeImage.context).load(it).into(holder.recipeImage)
            }
        }
        holder.itemView.setOnClickListener {
            listener.onRecipeClick(recipe)
        }

    }

    override fun getItemCount(): Int = recipeList.size

    interface Listener {
        fun onRecipeClick(recipe: RecipeItem)
    }

    // TODO Rename variables
    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val recipeName: TextView = mView.recipeName
        val recipeImage: ImageView = mView.imageRecipe
    }
}
