package com.mnvsngv.cookpedia.backend

import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mnvsngv.cookpedia.adapter.RecipeDisplayAdapter
import com.mnvsngv.cookpedia.dataclass.RecipeItem
import com.mnvsngv.cookpedia.dataclass.StepsItem
import com.mnvsngv.cookpedia.dataclass.User

private const val USERS_COLLECTION = "Users"
private const val RECIPES_COLLECTION = "Recipes"
var recipe_list: MutableList<RecipeItem> = mutableListOf()
private lateinit var recipe_collection : Query

class FirebaseBackend(private val backendListener: BackendListener) : Backend {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun updateUserDetails(user: User, user_id: String) {
        db.collection(USERS_COLLECTION).document(user.username)
            .set(user)
            .addOnSuccessListener {
                backendListener.loadCookpediaHome()
            }
    }

    override fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
                backendListener.onLoginSuccess()
            } else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.displayRegistrationErr()
            }
        }
    }

    override fun registerUser(
        email: String,
        password: String,
        fullName: String,
        username: String
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                Log.d("auth", "createUserWithEmail:success")
//                val user = auth.currentUser
//                val user_id = user?.uid
                backendListener.onRegisterSuccess()
            } else {
                Log.w("db", "createUserWithEmail:failure", task.exception)
                backendListener.displayRegistrationErr()
            }
        }
    }

    override fun readAllRecipes(searchstr: String): MutableList<RecipeItem> {

        if(searchstr.length == 0) {
            recipe_collection = db.collection(RECIPES_COLLECTION)

        } else if (searchstr.length > 0) {
            recipe_collection =
                    db.collection(RECIPES_COLLECTION).orderBy("name").startAt(searchstr.trim()).endAt(searchstr.trim()+"\uf8ff")
        }

        recipe_list.clear()
        recipe_collection.get().addOnSuccessListener { result ->
            for (documentSnapshot in result) {
                val steps_list = documentSnapshot.get("steps").to(StepsItem::class.java)
                val recipe_image = documentSnapshot.get("recipe_image")
                val recipe_name = documentSnapshot.get("name")

                val recipeItem =
                    RecipeItem(recipe_name as String, recipe_image as String, steps_list.first as ArrayList<StepsItem>)
                recipe_list.add(recipeItem)
                backendListener.notifyChange()
            }
        }
        return recipe_list
    }
}
