package com.mnvsngv.cookpedia.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.activity.helper.PhotoIntentCreator
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.fragment_add_recipe_photo.view.*

private const val GET_PHOTO = 1
private const val RECIPE_KEY = "recipe"

class AddRecipePhotoFragment : Fragment(), BackendListener {

    private val backend = BackendFactory.getInstance(this)
    private var listener: OnFragmentInteractionListener? = null
    private val photoHelper = PhotoIntentCreator()
    private lateinit var recipe: RecipeItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = arguments?.getParcelable(RECIPE_KEY) as RecipeItem
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_recipe_photo, container, false)

        Glide.with(view.recipeImage).load(R.drawable.ic_add_recipe_image_dark).submit()
        view.recipeImage.setOnClickListener { getRecipePhoto() }

        view.submitRecipeFab.setOnClickListener { backend.addRecipe(recipe) }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnFragmentInteractionListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_PHOTO -> {
                    data?.let {
                        photoHelper.photoUri = it.data as Uri
                    }

                }
            }
            Glide.with((view?.recipeImage) as View).load(photoHelper.photoUri).submit()
        }
    }

    private fun getRecipePhoto() {
        startActivity(photoHelper.newIntent(this.context as Context))
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun afterAddPhoto(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(recipe: RecipeItem): AddRecipePhotoFragment {
            return AddRecipePhotoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(RECIPE_KEY, recipe)
                }
            }
        }
    }
}
