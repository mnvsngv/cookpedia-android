package com.mnvsngv.cookpedia.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.activity.RecipeViewActivity
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.fragment.adapter.RecipeDisplayAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.fragment_recipe_list.view.*
import java.io.Serializable


// TODO Rename RECIPES_KEY and RECIPE_KEY and rearrange to use them globally & sensibly
const val RECIPES_KEY = "user_recipes"

// TODO Code cleanup
class RecipeListFragment : Fragment(), BackendListener, RecipeDisplayAdapter.Listener {

    private val backend = BackendFactory.getInstance(this)
    private lateinit var recipeList: List<RecipeItem>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("tagger", "onCreateView")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

//        recipe_recycler_view = view.findViewById(R.id.list)

//        Setting the Layout for the Recycler view to display user_recipes
//        view.list.hasFixedSize()
        view.list.layoutManager = LinearLayoutManager(context)
        view.list.adapter = RecipeDisplayAdapter(context, recipeList, this)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("tagger", "onCreate")
        recipeList = arguments?.getSerializable(RECIPES_KEY) as List<RecipeItem>
    }

    override fun onRecipeClick(recipe: RecipeItem) {
        val intent = Intent(this.context, RecipeViewActivity::class.java)
        intent.putExtra(RECIPE_KEY, recipe)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(recipes: List<RecipeItem>): RecipeListFragment {
            return RecipeListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(RECIPES_KEY, recipes as Serializable)
                }
            }
        }
    }
}
