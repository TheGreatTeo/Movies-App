package com.example.moviesapp.fragments.LogInActivityFragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.controller.AuthHandler
import com.google.firebase.auth.FirebaseAuth

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
        val authHandler = AuthHandler(auth,requireContext().applicationContext)
        val signUpButton:Button = view.findViewById(R.id.signUp)
        val emailText: EditText = view.findViewById(R.id.emailSignUp)
        val passwordText: EditText = view.findViewById(R.id.passwordSignUp)
        val confirmPasswordText: EditText = view.findViewById(R.id.confPassword)

        signUpButton.setOnClickListener{
            authHandler.registerUser(emailText,passwordText,confirmPasswordText,requireActivity())
        }

        return view
    }
}