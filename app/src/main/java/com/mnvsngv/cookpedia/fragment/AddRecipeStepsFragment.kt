package com.mnvsngv.cookpedia.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.RecipeStep
import com.mnvsngv.cookpedia.fragment.adapter.AddRecipeStepsAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.fragment_add_recipe_list.view.*
import java.io.Serializable


const val INGREDIENTS_KEY = "ingredients"

class AddRecipeStepsFragment : Fragment(), BackendListener, AddRecipeStepsAdapter.RecipeStepAdapterListener {

    private val steps: MutableList<RecipeStep> = ArrayList()
    private val backend = BackendFactory.getInstance(this)
    private lateinit var ingredients: ArrayList<RecipeIngredient?>
    private lateinit var listener : AddRecipeStepsListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            ingredients = it.getSerializable(com.mnvsngv.cookpedia.fragment.INGREDIENTS_KEY) as ArrayList<RecipeIngredient?>
        }
    }

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
                adapter = AddRecipeStepsAdapter(steps, this@AddRecipeStepsFragment)
            }
        }

        view.submitRecipeFab.setOnClickListener { listener.afterAddSteps(getRecipe()) }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as AddRecipeStepsListener
    }

    override fun onAddStep() {
        addStep()
    }

    private fun addStep() {
        val stepsCache = getRecipe().steps
        steps.removeAll { true }
        steps.addAll(stepsCache)
        steps.add(RecipeStep("", "", steps.size + 1))

        if (view?.list is RecyclerView) {
            with(view?.list) {
                if(this != null) {
                    adapter?.notifyDataSetChanged()
                    scrollToPosition(steps.size)
                }
            }
        }
    }

    private fun getRecipe(): RecipeItem {
        if (view?.list is RecyclerView) {
            with(view?.list as RecyclerView) {

                val finalSteps = ArrayList<RecipeStep>()
                for (i in 0 until (adapter as AddRecipeStepsAdapter).itemCount - 1) {
                    val nextStep = getChildViewHolder(getChildAt(i))
                    if (nextStep is AddRecipeStepsAdapter.ViewHolder) {
                        val step = nextStep.mContentView?.text.toString()
                        if (step.isNotEmpty()) {
                            finalSteps.add(RecipeStep(step, "", finalSteps.size + 1))
                        }
                    }
                }

                return RecipeItem(
                    view?.recipeNameInput?.text.toString(),
                    "",
                    finalSteps,
                    ingredients as List<RecipeIngredient>
                )
            }
        }
        return RecipeItem("", "", arrayListOf(), listOf())
    }

    interface AddRecipeStepsListener {
        fun afterAddSteps(recipe: RecipeItem)
    }

    companion object {
        @JvmStatic
        fun newInstance(ingredients: List<RecipeIngredient>): AddRecipeStepsFragment {
            return AddRecipeStepsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(INGREDIENTS_KEY, ingredients as ArrayList<Parcelable>)
                }
            }
        }
    }
}
