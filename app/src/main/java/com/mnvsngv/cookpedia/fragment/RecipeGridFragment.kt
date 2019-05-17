package com.mnvsngv.cookpedia.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.activity.RecipeViewActivity
import com.mnvsngv.cookpedia.backend.Backend
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.adapter.RecipeGridViewAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory

// TODO Code cleanup
class RecipeGridFragment : Fragment(), BackendListener, RecipeGridViewAdapter.RecipeDisplayAdapterListener {

    var recipe_list: MutableList<RecipeItem> = mutableListOf()
    private lateinit var backend : Backend
    private var recipeAdapter: RecipeGridViewAdapter ?= null
    private var recipe_recycler_view: RecyclerView ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mFragView = inflater.inflate(R.layout.fragment_recipe_grid, container, false)
        recipe_recycler_view = mFragView.findViewById(R.id.list)
        recipe_recycler_view?.layoutManager = GridLayoutManager(context,2)


//        recipeList = backend.readAllRecipes()
        recipeAdapter = RecipeGridViewAdapter(context, recipe_list, this)
        recipe_recycler_view?.adapter = recipeAdapter

        return mFragView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backend = BackendFactory.getInstance(this)
    }

    override fun notifyChange() {
        recipeAdapter?.notifyDataSetChanged()
    }

    override fun onRecipeClick(recipe: RecipeItem) {
        val intent = Intent(this.context, RecipeViewActivity::class.java)
        intent.putExtra("HOOYOO", recipe)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(): RecipeGridFragment {
            return RecipeGridFragment()
        }
    }
}
