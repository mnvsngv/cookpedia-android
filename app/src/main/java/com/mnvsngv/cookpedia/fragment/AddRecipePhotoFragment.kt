package com.mnvsngv.cookpedia.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.activity.helper.GET_PHOTO
import com.mnvsngv.cookpedia.activity.helper.PhotoCapture
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.fragment_add_recipe_photo.view.*

const val RECIPE_KEY = "recipe"

class AddRecipePhotoFragment : Fragment(), BackendListener {

    private val backend = BackendFactory.getInstance(this)
    private lateinit var listener: OnFragmentInteractionListener
    private val photoHelper = PhotoCapture()
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

        view.submitRecipeFab.setOnClickListener {
            backend.addRecipe(recipe)
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnFragmentInteractionListener
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        photoHelper.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRecipeUploadSuccess() {
//        photoHelper.deletePhoto()
        listener.afterRecipeUpload()
    }

    private fun getRecipePhoto() {
        startActivityForResult(photoHelper.newIntent(this.context as Context) {
            listener.afterAddPhoto(photoHelper.photoUri)
            with ((view?.recipeImage) as ImageView) {
                Glide.with(this.context as Context).load(photoHelper.photoUri).into(this)
                alpha = 1f
            }
        }, GET_PHOTO)
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun afterAddPhoto(uri: Uri)
        fun afterRecipeUpload()
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
