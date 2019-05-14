package com.mnvsngv.cookpedia.fragment

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.RecipeStep
import com.mnvsngv.cookpedia.fragment.adapter.RecipeStepAdapter
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.fragment_add_recipe_list.view.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val GET_PHOTO = 1

class AddRecipeStepsFragment : Fragment(), BackendListener, RecipeStepAdapter.RecipeStepAdapterListener {

    private val steps: MutableList<RecipeStep> = ArrayList()
    private val backend = BackendFactory.getInstance(this)
    private lateinit var photoUri: Uri

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
                adapter = RecipeStepAdapter(steps, this@AddRecipeStepsFragment)
            }
        }

        view.submitRecipeFab.setOnClickListener { submitRecipe() }
        Glide.with(view.recipeImage).load(R.drawable.ic_add_recipe_image_dark).submit()

        view.recipeImage.setOnClickListener { getRecipePhoto() }

        return view
    }

    override fun onAddStep() {
        addStep()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_PHOTO -> {
                    data?.let {
                        photoUri = it.data as Uri
                    }

                    view?.recipeImage?.setImageURI(photoUri)
                }
            }
        }
    }

    private fun addStep() {
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

    private fun submitRecipe() {
        if (view?.list is RecyclerView) {
            with(view?.list as RecyclerView) {

                val finalSteps = ArrayList<RecipeStep>()
                for (i in 0 until (adapter as RecipeStepAdapter).itemCount) {
                    val nextStep = getChildViewHolder(getChildAt(i))
                    if (nextStep is RecipeStepAdapter.ViewHolder) {
                        finalSteps.add(RecipeStep(nextStep.mContentView?.text.toString(), "", finalSteps.size + 1))
                    }
                }

                backend.addRecipe(RecipeItem(view?.recipeNameInput?.text.toString(), steps=finalSteps))
            }
        }
    }

    private fun getRecipePhoto() {
        startActivityForResult(getPickImageChooserIntent(), GET_PHOTO)
    }

    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    /**
     * Create a chooser intent to select the source to get image from.<br></br>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br></br>
     * All possible sources are added to the intent chooser.
     */
    fun getPickImageChooserIntent(): Intent {

        // Determine Uri of camera image to save.
        photoUri = FileProvider.getUriForFile(
            this.context as Context,
            "com.mnvsngv.cookpedia.fileprovider",
            createImageFile(this.context as Context)
        )

        val allIntents = ArrayList<Intent>()
        val packageManager = context?.packageManager as PackageManager

        // collect all camera intents
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            allIntents.add(intent)
        }

        // collect all gallery intents
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            allIntents.add(intent)
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        var mainIntent: Intent = allIntents.get(allIntents.size - 1)
        for (intent in allIntents) {
            if (intent.getComponent()!!.getClassName() == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)

        // Create a chooser from the main intent
        val chooserIntent = Intent.createChooser(mainIntent, "Select source")

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())

        return chooserIntent
    }
}
