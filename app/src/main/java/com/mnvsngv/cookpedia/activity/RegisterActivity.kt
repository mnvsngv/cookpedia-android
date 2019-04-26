package com.mnvsngv.cookpedia.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.mnvsngv.cookpedia.DataClass.UserItem
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.Backend
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), BackendListener {

    private lateinit var backend :Backend
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        backend = BackendFactory.getInstance(this)
        loginButton.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show()
            startActivity(loginIntent)
        }

        registerButton.setOnClickListener {
            // On clicking the register button, the progress bar is loaded.
            // Also a mandatory check is performed for all the user input fields in the registration form
            progressBar.visibility = View.VISIBLE
            if (!areInputsValid()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else {
                register(
                    emailInput.text.toString(),
                    passwordInput.text.toString()
                )
            }
        }
    }

    private fun register(email: String, password: String) {
        backend.authenticateUser(email, password)
    }

    private fun areInputsValid(): Boolean {
        val isValidEmail = validate(emailInput, R.string.invalid_email) {
            TextUtils.isEmpty(it) || !Patterns.EMAIL_ADDRESS.matcher(it).matches()
        }

        // The validate() call is done before the AND with isValid.
        // This is because if isValid is false, then validate() is not called as a "shortcut".
        // In that case the user would not see all the invalid inputs at the same time.
        val isValidPass = validate(passwordInput, R.string.invalid_password) {
            TextUtils.isEmpty(it)
        }

        val isValidFullname = validate(fullname, R.string.invalid_fullname) {
            TextUtils.isEmpty(it)
        }

        val isValidUsername = validate(username, R.string.invalid_username) {
            TextUtils.isEmpty(it)
        }

        return isValidEmail && isValidPass && isValidFullname && isValidUsername
    }

    // Run an invalidation test on a given text field
    private fun validate(view: TextView, errorMessageID: Int, isInvalid: (String) -> Boolean): Boolean {
        val textToValidate = view.text.toString()
        return if (isInvalid(textToValidate)) {
            view.error = getString(errorMessageID)
            false
        } else true
    }


    // Return a map of User Details to the backend
    override fun getUserDetails(user_id: String?): HashMap<String, Any> {
        if (user_id == null) {
            Toast.makeText(
                this, "Authentication failed.", Toast.LENGTH_SHORT
            ).show()
        }
        val map: HashMap<String, Any> = hashMapOf(
            "id" to user_id.toString(),
            "fullname" to fullname.text.toString(),
            "username" to username.text.toString(),
            "password" to passwordInput.text.toString(),
            "email" to emailInput.text.toString()
        )
        return map
    }

    override fun loadCookpediaHome() {
        val loadCookpedia = Intent(this, HomeActivity::class.java)
        startActivity(loadCookpedia)
        finish()
    }

    override fun displayRegistrationErr() {
        Toast.makeText(this, "User cannot be registered. Please check the details!!", Toast.LENGTH_SHORT).show()
    }

}
