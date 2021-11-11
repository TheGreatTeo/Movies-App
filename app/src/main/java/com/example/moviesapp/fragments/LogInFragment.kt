package com.example.moviesapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.controller.AuthHandler
import com.example.moviesapp.controller.callback
import com.google.firebase.auth.FirebaseAuth

class LogInFragment : Fragment(R.layout.login_fragment) {

    var callbackFragment:callback? = null
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
        var email: EditText = view.findViewById(R.id.email)
        var password: EditText = view.findViewById(R.id.password)

        logInButton.setOnClickListener{
            Log.d("LogIn","DA")
            authHandler.userLogIn(email!!,password!!)
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
        Log.d("onSaveInstanceState","ceva")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState != null){
            Log.d("onActivityCreated","ceva")
        }
    }
}