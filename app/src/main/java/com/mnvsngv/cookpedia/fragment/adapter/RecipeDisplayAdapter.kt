package com.mnvsngv.cookpedia.fragment.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
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
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.recipelist_item.view.*

// TODO Code cleanup
class RecipeDisplayAdapter(
    private val context: Context?,
    private val recipeList: List<RecipeItem>,
    private val listener: Listener
) : RecyclerView.Adapter<RecipeDisplayAdapter.MyViewHolder>(), BackendListener {

    val backend = BackendFactory.getInstance(this)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recipelist_item, parent, false)

        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

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

    override fun getItemCount(): Int {
        return recipeList.size
    }

    interface Listener {
        fun onRecipeClick(recipe: RecipeItem)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeName: TextView = view.recipe_name
        val recipeImage: ImageView = view.dish_image
    }
}