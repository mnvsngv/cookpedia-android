package com.mnvsngv.cookpedia.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.mnvsngv.cookpedia.DataClass.UserItem
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), BackendListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //TODO
        val backend = BackendFactory.getInstance(this)
        loginButton.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
            startActivity(loginIntent)
        }

        registerButton.setOnClickListener {
            // On clicking the register button, the progress bar is loaded.
            // Also a mandatory check is performed for all the user input fields in the registration form
            progressBar.visibility = View.VISIBLE
            if (areInputsInvalid()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else {
                register(
                    emailInput.text.toString(),
                    fullname.text.toString(),
                    username.text.toString(),
                    passwordInput.text.toString()
                )
            }
        }
    }

    private fun register(email: String, fullname: String, username: String, password: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun areInputsInvalid(): Boolean {
        return (TextUtils.isEmpty(emailInput.text) || TextUtils.isEmpty(username.text) || TextUtils.isEmpty(fullname.text)
                || TextUtils.isEmpty(passwordInput.text))
    }

    override fun getUserDetails(user_id: String?): HashMap<String, Any> {
        if (user_id == null) {
            Toast.makeText(
                this, "Authentication failed.", Toast.LENGTH_SHORT
            ).show()
        }
        val map: HashMap<String, Any> = hashMapOf(
            "id" to user_id.toString(),
            "fullname" to fullname,
            "username" to username,
            "password" to passwordInput,
            "email" to emailInput
        )
        return map
    }
}
