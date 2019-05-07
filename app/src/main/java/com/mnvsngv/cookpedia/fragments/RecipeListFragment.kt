package com.mnvsngv.cookpedia.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.adapter.RecipeDisplayAdapter
import com.mnvsngv.cookpedia.backend.*
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.fragment_recipe_list.*


class RecipeListFragment : Fragment(), BackendListener {

    private lateinit var backend : Backend
    private lateinit var mSearchtext : EditText
    private lateinit var recipe_recycler_view: RecyclerView
    private var recipeAdapter: RecipeDisplayAdapter ?= null
    var recipe_list: MutableList<RecipeItem> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var mFragView = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        recipe_recycler_view = mFragView.findViewById(R.id.recipelist_view)
        mSearchtext = mFragView.findViewById(R.id.search_text)

//        Setting the Layout for the Recycler view to display recipes
        val mLayoutManager = LinearLayoutManager(context)
        recipe_recycler_view.setLayoutManager(mLayoutManager)
        recipe_recycler_view.hasFixedSize()

//        Reading all recipes from the backend and displaying them in the recycler view
        recipe_list = backend.readAllRecipes("")
        recipeAdapter = RecipeDisplayAdapter(context, recipe_list)
        recipe_recycler_view.adapter = recipeAdapter

//        List the searched recipes
        mSearchtext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                backend.readAllRecipes(s.toString().toLowerCase())
            }
        })

        return mFragView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        backend = BackendFactory.getInstance(this)

    }

    override fun notifyChange() {
        recipeAdapter?.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(): RecipeListFragment {
            return RecipeListFragment()
        }
    }
}
