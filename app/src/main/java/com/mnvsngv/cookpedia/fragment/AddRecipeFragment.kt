package com.mnvsngv.cookpedia.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.RecipeStep
import com.mnvsngv.cookpedia.fragment.adapter.RecipeStepAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory

import kotlinx.android.synthetic.main.fragment_add_recipe_list.view.*

class AddRecipeFragment : Fragment(), BackendListener {

    private val steps: MutableList<RecipeStep> = ArrayList()
    private val backend = BackendFactory.getInstance(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe_list, container, false)

        addStep()

        // Set the adapter
        if (view.list is RecyclerView) {
            with(view.list) {
                layoutManager = LinearLayoutManager(context)
                adapter = RecipeStepAdapter(steps)
            }
        }

        view.addStepButton.setOnClickListener { addStep() }
        view.submitRecipeButton.setOnClickListener { submitRecipe() }
        return view
    }

    private fun addStep() {
        steps.add(RecipeStep("", "", steps.size + 1))

        if (view?.list is RecyclerView) {
            with(view?.list) {
                if(this != null) adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun submitRecipe() {
        if (view?.list is RecyclerView) {
            with(view?.list as RecyclerView) {

                val finalSteps = ArrayList<RecipeStep>()
                for (i in 0 until (adapter as RecipeStepAdapter).itemCount) {
                    val nextStep = getChildViewHolder(getChildAt(i))
                    if (nextStep is RecipeStepAdapter.ViewHolder) {
                        finalSteps.add(RecipeStep(nextStep.mContentView.text.toString(), "", finalSteps.size + 1))
                    }
                }

                backend.addRecipe(RecipeItem(view?.recipeNameInput?.text.toString(), steps=finalSteps))
            }
        }
    }
}
