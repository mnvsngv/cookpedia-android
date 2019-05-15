package com.mnvsngv.cookpedia.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.User

private const val USERS_COLLECTION = "Users"
private const val RECIPES_COLLECTION = "Recipes"
private var recipeList: MutableList<RecipeItem> = mutableListOf()
private lateinit var docRef: DocumentReference

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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

    override fun readAllRecipes(): MutableList<RecipeItem> {
        var recipeCollection = db.collection(RECIPES_COLLECTION)

        recipeList.clear()
        recipeCollection.get().addOnSuccessListener { result ->
            for (document in result) {
                val recipeItem = document.toObject(RecipeItem::class.java)
                recipeList.add(recipeItem)
                backendListener.notifyChange()
            }
        }
        return recipeList
    }

    override fun readUserRecipes(): MutableList<RecipeItem> {
        var current_user = auth.currentUser?.email

        current_user?.let {
            db.collection(USERS_COLLECTION).document(current_user).get().addOnSuccessListener { task ->
                recipeList = task.get("user_recipes") as MutableList<RecipeItem>
                backendListener.notifyChange()
            }
        }
        return recipeList
    }

    override fun addRecipe(recipe: RecipeItem) {
        docRef = db.collection(RECIPES_COLLECTION).document()
        docRef.set(recipe)
        updateUserRecipes(recipe)
    }

    override fun updateUserRecipes(recipe: RecipeItem) {
        var current_user = auth.currentUser?.email

        current_user?.let {
            db.collection(USERS_COLLECTION).document(current_user).update("user_recipes", FieldValue.arrayUnion(recipe))
        }
    }

}
