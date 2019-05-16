package com.mnvsngv.cookpedia.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.activity.helper.PhotoCapture
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

    private val steps = ArrayList<RecipeStep>()
    private val photoHelpers = ArrayList<PhotoCapture>()
    private val backend = BackendFactory.getInstance(this)
    private lateinit var ingredients: ArrayList<RecipeIngredient?>
    private lateinit var stepsListener : AddRecipeStepsListener

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

        view.submitRecipeFab.setOnClickListener {
            val recipe = RecipeItem(
                view?.recipeNameInput?.text.toString(),
                "",
                steps,
                ingredients
            )
            stepsListener.afterAddSteps(recipe)
        }

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        stepsListener = context as AddRecipeStepsListener
    }

    override fun onAddStep() {
        addStep()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        photoHelpers[requestCode].onActivityResult(requestCode, resultCode, data)
    }

    override fun onAddPhotoFor(step: RecipeStep) {
        val stepIndex = step.stepNumber-1
        val helper = photoHelpers[stepIndex]
        startActivityForResult(helper.newIntent(this.context as Context) {
//            stepsListener.afterAddPhoto(helper.photoUri)
//            with ((view?.recipeImage) as ImageView) {
//                Glide.with(this.context as Context).load(photoHelper.photoUri).into(this)
//                alpha = 1f
//            }
            steps[stepIndex].imageSource = helper.photoUri.toString()
            view?.list?.adapter?.notifyDataSetChanged()
        }, stepIndex)
    }

    private fun addStep() {
        photoHelpers.add(PhotoCapture(steps.size))
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

    // TODO Listener
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
