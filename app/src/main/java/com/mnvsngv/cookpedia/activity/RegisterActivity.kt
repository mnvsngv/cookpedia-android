package com.mnvsngv.cookpedia.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), BackendListener, TextView.OnEditorActionListener {

    private val backend = BackendFactory.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener { register() }
        passwordInput.setOnEditorActionListener(this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            register()
            return true  // Event handled!
        }
        return false  // Event not handled!
    }

    private fun register() {
        // On registering, the progress bar is loaded.
        // Also a mandatory check is performed for all the user input fields in the registration form
        if (areInputsValid()) {
            progressBar.visibility = View.VISIBLE
            backend.registerUser(
                emailInput.text.toString(),
                passwordInput.text.toString(),
                fullname.text.toString(),
                username.text.toString()
            )
        }
    }

    private fun areInputsValid(): Boolean {
        // The validate() call is done before the AND with isValid.
        // This is because if isValid is false, then validate() is not called as a "shortcut".
        // In that case the user would not see all the invalid inputs at the same time.

        var isValid = validate(emailInput, R.string.invalid_email) {
            TextUtils.isEmpty(it) || !Patterns.EMAIL_ADDRESS.matcher(it).matches()
        }

        isValid = validate(passwordInput, R.string.invalid_password) {
            TextUtils.isEmpty(it)
        } && isValid

        isValid = validate(fullname, R.string.invalid_fullname) {
            TextUtils.isEmpty(it)
        } && isValid

        isValid = validate(username, R.string.invalid_username) {
            TextUtils.isEmpty(it)
        } && isValid

        return isValid
    }

    // Run an invalidation test on a given text field
    private fun validate(view: TextView, errorMessageID: Int, isInvalid: (String) -> Boolean): Boolean {
        val textToValidate = view.text.toString()
        return if (isInvalid(textToValidate)) {
            view.error = getString(errorMessageID)
            false
        } else true
    }


    override fun onRegisterSuccess() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    override fun onRegisterFailure() {
        Toast.makeText(this, R.string.registration_failure, Toast.LENGTH_SHORT).show()
    }

}
