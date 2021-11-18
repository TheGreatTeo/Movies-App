package com.example.moviesapp.fragments.LogInActivityFragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.moviesapp.Activities.LogInActivity
import com.example.moviesapp.Activities.MainActivity
import com.example.moviesapp.R
import com.example.moviesapp.controller.ActivityOpener
import com.example.moviesapp.controller.AuthHandler
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.controller.callback
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LogInFragment : Fragment(R.layout.login_fragment) {

    var callbackFragment:callback? = null
    val sharedPrefsHandler = SharedPrefsHandler()
    val activityOpener = ActivityOpener()
    private var email: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment,container,false)

        val auth = FirebaseAuth.getInstance()
        val authHandler = AuthHandler(auth,view.context.applicationContext)
        var logInButton: Button = view.findViewById(R.id.logInButton)
        var signUpButton: Button = view.findViewById(R.id.signUp)
        email = view.findViewById(R.id.email)
        var password: EditText = view.findViewById(R.id.password)
        var forgotPassword: TextView = view.findViewById(R.id.forgotPassword)
        var emailCopy = email
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        logInButton.setOnClickListener {
            val emailText = email!!.text.toString()
            val passwordText = password.text.toString()
            lifecycleScope.launch {
                if (emailText.isEmpty()) {
                    email!!.setError("Email is requierd")
                    email!!.requestFocus()
                    return@launch
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    email!!.setError("Invalid email")
                    email!!.requestFocus()
                    return@launch
                }
                if (passwordText.isEmpty()) {
                    password.setError("Password is requierd")
                    password.requestFocus()
                    return@launch
                }
                authHandler.userLogIn(emailText, passwordText, requireActivity(),progressBar)

            }
        }

        signUpButton.setOnClickListener{
            callbackFragment?.changeFragment()
        }


        return view
    }

    fun getCallbackFragment(callbackFragment: callback){
        this.callbackFragment = callbackFragment
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("email",email?.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("LogIn","AICI")
        var emailText = ""
        if(savedInstanceState != null){
            emailText = savedInstanceState.getString("email").toString()
            email!!.setText(emailText)
        }
    }
}