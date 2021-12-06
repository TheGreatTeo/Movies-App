package com.example.moviesapp.fragments.LogInActivityFragments

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.moviesapp.R
import com.example.moviesapp.controller.AuthHandler
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignUpFragment : Fragment(R.layout.signup_fragment) {

    override fun onAttach(context: Context) {
        var sharedPrefs = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        var editor = sharedPrefs.edit()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signup_fragment, container, false)

        val auth = FirebaseAuth.getInstance()
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val authHandler = AuthHandler(auth,requireContext().applicationContext)
        val signUpButton:Button = view.findViewById(R.id.signUp)
        val emailText: EditText = view.findViewById(R.id.emailSignUp)
        val passwordText: EditText = view.findViewById(R.id.passwordSignUp)
        val confirmPasswordText: EditText = view.findViewById(R.id.confPassword)
        val usernameText: EditText = view.findViewById(R.id.username)

        signUpButton.setOnClickListener {
            val email = emailText!!.text.toString()
            val password = passwordText.text.toString()
            val passwordConf = confirmPasswordText.text.toString()
            val username = usernameText.text.toString()
            lifecycleScope.launch {
                if (email.isEmpty()) {
                    emailText!!.setError("Email is requierd")
                    emailText!!.requestFocus()
                    return@launch
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText!!.setError("Invalid email")
                    emailText!!.requestFocus()
                    return@launch
                }
                if (password.isEmpty()) {
                    passwordText.setError("Password is requierd")
                    passwordText.requestFocus()
                    return@launch
                }
                if (password.length < 6) {
                    passwordText.setError("Min 6 characters")
                    passwordText.requestFocus()
                    return@launch
                }
                if (passwordConf.isEmpty()) {
                    confirmPasswordText.setError("Password is requierd")
                    confirmPasswordText.requestFocus()
                    return@launch
                }
                if (!password.equals(passwordConf)) {
                    confirmPasswordText.setError("Passwords not matching")
                    confirmPasswordText.requestFocus()
                    return@launch
                }
                if (username.isEmpty()) {
                    usernameText.setError("Username is requierd")
                    usernameText.requestFocus()
                    return@launch
                }
                if (username.length < 6) {
                    usernameText.setError("Min 6 characters")
                    usernameText.requestFocus()
                    return@launch
                }
                emailText.visibility = View.GONE
                passwordText.visibility = View.GONE
                confirmPasswordText.visibility = View.GONE
                usernameText.visibility = View.GONE
                signUpButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                authHandler.registerUser(
                    emailText,
                    passwordText,
                    confirmPasswordText,
                    usernameText,
                    requireActivity()
                )
            }
        }

        return view
    }
}