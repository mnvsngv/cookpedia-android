package com.mnvsngv.cookpedia.backend

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mnvsngv.cookpedia.dataclass.RecipeIngredient
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.User


private const val USERS_COLLECTION = "Users"
private const val RECIPES_COLLECTION = "Recipes"

// TODO Remove this variable
private var recipeList: List<RecipeItem> = arrayListOf()


// TODO Code cleanup
class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("auth", "loginUserWithEmail:success")
                backendListener.onLoginSuccess()
            } else {
                Log.d("auth", "loginUserWithEmail:failure")
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
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.onRegisterFailure()
            }
        }
    }

    override fun readAllRecipes() {
        var recipeCollection = db.collection(RECIPES_COLLECTION)

        recipeCollection.get().addOnSuccessListener { result ->
            val recipes = ArrayList<RecipeItem>()
            for (document in result) {
                val recipeItem = document.toObject(RecipeItem::class.java)
                recipes.add(recipeItem)
            }
            backendListener.onReadAllRecipes(recipes)
        }
    }

    override fun readUserRecipes() {
        var current_user = auth.currentUser?.email

        current_user?.let {
            db.collection(USERS_COLLECTION).document(current_user).get().addOnCompleteListener { task ->
                var user = task.result?.toObject(User::class.java)
                user?.let {
                    backendListener.onReadAllRecipes(it.user_recipes)
                }
            }
        }
    }

    override fun addRecipe(recipe: RecipeItem) {
        val photoUri = Uri.parse(recipe.image)
        val storageRef = storage.reference


        if (photoUri.lastPathSegment != null) {
            recipe.image = photoUri.lastPathSegment as String
        }

        db.collection(RECIPES_COLLECTION)
            .add(recipe)
            .addOnSuccessListener { documentReference ->
                val id = auth.currentUser?.email
                val photoRef = storageRef.child("$id/${photoUri.lastPathSegment}")
                photoRef.putFile(photoUri)
                    .addOnCompleteListener {
                        updateUserRecipes(recipe)
                        backendListener.onRecipeUploadSuccess()
                    }
            }
    }

    override fun updateUserRecipes(recipe: RecipeItem) {
        var current_user = auth.currentUser?.email

        current_user?.let {
            db.collection(USERS_COLLECTION).document(current_user).update("user_recipes", FieldValue.arrayUnion(recipe))
        }
    }

    override fun getAllIngredients() {

        db.collection(RECIPES_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val ingredients = HashSet<RecipeIngredient>()
                for (document in result) {
                    val recipeItem = document.toObject(RecipeItem::class.java)
                    ingredients.addAll(recipeItem.ingredients)
                }
                backendListener.onGetAllIngredients(ingredients.toList())
            }
    }

    override fun searchRecipesUsing(ingredientsToSearch: List<RecipeIngredient>) {
        db.collection(RECIPES_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                val validRecipes = ArrayList<RecipeItem>()
                for (document in snapshot) {
                    val recipe = document.toObject(RecipeItem::class.java)
                    if (recipe.ingredients.containsAll(ingredientsToSearch)) {
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
