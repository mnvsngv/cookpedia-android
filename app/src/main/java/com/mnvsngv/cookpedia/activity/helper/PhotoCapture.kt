package com.mnvsngv.cookpedia.activity.helper

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


const val GET_PHOTO = 1


// TODO Manav: Review & document this class with comments
class PhotoCapture {

    var photoUri: Uri? = null
    private lateinit var context: Context
    private lateinit var onPhotoCapture: () -> Unit

    fun newIntent(context: Context, onPhotoCapture: () -> Unit): Intent {

        this.context = context
        this.onPhotoCapture = onPhotoCapture

        // Determine Uri of camera image to save.
        photoUri = FileProvider.getUriForFile(
            context,
            "com.mnvsngv.cookpedia.fileprovider",
            createImageFile(context)
        )

        val allIntents = ArrayList<Intent>()
        val packageManager = context.packageManager as PackageManager

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
            if (intent.component?.className == "com.android.documentsui.DocumentsActivity") {
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

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_PHOTO -> {
                    data?.let {
                        photoUri = it.data as Uri
                    }

                }
            }

            onPhotoCapture()
        }
    }

    fun deletePhoto() {
        photoUri?.let { uri ->
            if (uri.authority == "com.mnvsngv.mnvsngv.fileprovider") {
                // Clean up after upload
                context.contentResolver.delete(uri, null, null)
            }
        }
    }

    private fun createImageFile(context: Context): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }
}