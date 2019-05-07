package com.mnvsngv.cookpedia.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.User

private const val USERS_COLLECTION = "Users"
private const val RECIPES_COLLECTION = "Recipes"
private var recipeList: MutableList<RecipeItem> = mutableListOf()
private lateinit var recipeCollection : Query

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task->
            if(task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                backendListener.onLoginSuccess()
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.onRegisterFailure()
            }
        }
    }

    override fun registerUser(email: String, password: String, fullName: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task->
            if(task.isSuccessful) {
                db.collection(USERS_COLLECTION).document(username)
                    .set(User(email, username, fullName))
                    .addOnSuccessListener {
                        backendListener.onRegisterSuccess()
                    }
            }
            else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.onRegisterFailure()
            }
        }
    }

    override fun readAllRecipes(searchstr: String): MutableList<RecipeItem> {

        if(searchstr.length == 0) {
            recipeCollection = db.collection(RECIPES_COLLECTION)

        } else if (searchstr.length > 0) {
            recipeCollection =
                    db.collection(RECIPES_COLLECTION).orderBy("name").startAt(searchstr.trim()).endAt(searchstr.trim()+"\uf8ff")
        }

        recipeList.clear()
        recipeCollection.get().addOnSuccessListener { result ->
            for (document in result) {
                val recipeItem = document.toObject(RecipeItem::class.java)
//                val steps_list = document.get("steps").to(RecipeStep::class.java)
//                val recipe_image = document.get("image")
//                val recipe_name = document.get("name")

//                val recipeItem =
//                    RecipeItem(recipe_name as String, recipe_image as String, steps_list.first as ArrayList<RecipeStep>)
                recipeList.add(recipeItem)
                backendListener.notifyChange()
            }
        }
        return recipeList
    }

    override fun addRecipe(recipe: RecipeItem) {
        db.collection(RECIPES_COLLECTION).document()
            .set(recipe)
    }
}
