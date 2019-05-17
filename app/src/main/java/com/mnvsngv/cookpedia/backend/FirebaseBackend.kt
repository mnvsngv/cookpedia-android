package com.mnvsngv.cookpedia.backend

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.User


private const val USERS_COLLECTION = "Users"
private const val RECIPES_COLLECTION = "Recipes"


class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                backendListener.onLoginSuccess()
            } else {
                backendListener.onLoginFailure()
            }
        }
    }

    override fun registerUser(email: String, password: String, fullName: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                db.collection(USERS_COLLECTION).document(email)
                    .set(User(email, username, fullName, arrayListOf()))
                    .addOnSuccessListener {
                        backendListener.onRegisterSuccess()
                    }
            } else {
                backendListener.onRegisterFailure()
            }
        }
    }

    override fun readAllRecipes() {
        db.collection(RECIPES_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val recipes = ArrayList<RecipeItem>()
                for (document in result) {
                    val recipeItem = document.toObject(RecipeItem::class.java)
                    recipes.add(recipeItem)
                }
                backendListener.onReadAllRecipes(recipes)
            }
    }

    override fun readUserRecipes() {
        val currentEmail = auth.currentUser?.email

        currentEmail?.let {
            db.collection(USERS_COLLECTION).document(currentEmail).get().addOnCompleteListener { task ->
                val user = task.result?.toObject(User::class.java)
                user?.let { backendListener.onReadAllRecipes(user.user_recipes) }
            }
        }
    }

    override fun addRecipe(recipe: RecipeItem) {
        val photoUri = Uri.parse(recipe.image)
        val stepUris = ArrayList<Uri>()

        for (step in recipe.steps) {
            if (step.imageSource.isNotEmpty()) {
                val uri = Uri.parse(step.imageSource)
                stepUris.add(uri)
                uri.lastPathSegment?.let {
                    step.imageSource = it
                }
            }
        }

        if (photoUri.lastPathSegment != null) {
            recipe.image = photoUri.lastPathSegment as String
        }
        val storageRef = storage.reference

        db.collection(RECIPES_COLLECTION)
            .add(recipe)
            .addOnSuccessListener { documentReference ->
                val id = auth.currentUser?.email
                val photoRef = storageRef.child("$id/${photoUri.lastPathSegment}")
                val task = photoRef.putFile(photoUri)

                for (uri in stepUris) {
                    task.continueWithTask {
                        storageRef.child("$id/${uri.lastPathSegment}")
                            .putFile(uri)
                    }
                }

                task.addOnCompleteListener {
                            updateUserRecipes(recipe)
                            backendListener.onRecipeUploadSuccess()
                        }
                }
    }

    override fun updateUserRecipes(recipe: RecipeItem) {
        val currentEmail = auth.currentUser?.email

        currentEmail?.let {
            db.collection(USERS_COLLECTION)
                .document(currentEmail)
                .update("user_recipes", FieldValue.arrayUnion(recipe))
        }
    }

    override fun getAllIngredients() {
        db.collection(RECIPES_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val ingredientNames = HashSet<String>()
                val ingredients = HashSet<RecipeIngredient>()
                for (document in result) {
                    val recipeItem = document.toObject(RecipeItem::class.java)
                    for (ingredient in recipeItem.ingredients) {
                        if (!ingredientNames.contains(ingredient.name)) {
                            ingredients.add(ingredient)
                            ingredientNames.add(ingredient.name)
                        }
                    }
                }
                backendListener.onGetAllIngredients(ingredients.toList().sortedBy { it.name })
            }
    }

    override fun searchRecipesUsing(ingredientsToSearch: List<RecipeIngredient>) {
        val ingredientNames = ingredientsToSearch.map { it.name }

        db.collection(RECIPES_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                val validRecipes = ArrayList<RecipeItem>()
                for (document in snapshot) {
                    val recipe = document.toObject(RecipeItem::class.java)
                    val candidateIngredients = recipe.ingredients.map { it.name }

                    if (candidateIngredients.containsAll(ingredientNames)) {
                        validRecipes.add(recipe)
                    }

                }
                backendListener.onSearchRecipesUsing(validRecipes)
            }
    }

    override fun loadImageFrom(path: String, run: (uri: Uri) -> Unit) {
        auth.currentUser?.email?.let {
            storage.reference.child(it).child(path).downloadUrl.addOnSuccessListener {uri ->
                run(uri)
            }
        }

    }

}
