package com.mnvsngv.cookpedia.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.mnvsngv.cookpedia.R
import com.mnvsngv.cookpedia.backend.BackendListener
import com.mnvsngv.cookpedia.singleton.BackendFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), TextView.OnEditorActionListener, BackendListener {

    private val backend = BackendFactory.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            if (areInputsValid()) {
                backend.loginUser(emailInput.text.toString(), passwordInput.text.toString())
            }
        }

        registerButton.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        // Initiate login when the user presses enter when on the password field
        passwordInput.setOnEditorActionListener(this)
    }

    // TODO Handle ENTER button pressed on the password field
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            if (areInputsValid()) {
                backend.loginUser(emailInput.text.toString(), passwordInput.text.toString())
            }
            return true  // Event handled!
        }

        return false  // Event not handled!
    }

    override fun onLoginSuccess() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }


    private fun areInputsValid(): Boolean {
        var isValid = validate(emailInput, R.string.invalid_email) {
            TextUtils.isEmpty(it) || !Patterns.EMAIL_ADDRESS.matcher(it).matches()
        }

        // The validate() call is done before the AND with isValid.
        // This is because if isValid is false, then validate() is not called as a "shortcut".
        // In that case the user would not see all the invalid inputs at the same time.
        isValid = validate(passwordInput, R.string.invalid_password) {
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

    override fun onLoginFailure() {
        Log.d("auth", "Register failure invoked")
        Toast.makeText(this, "User cannot Login. Please check the email and password!!", Toast.LENGTH_SHORT).show()
    }
}
