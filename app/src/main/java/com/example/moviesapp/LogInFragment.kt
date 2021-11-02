package com.example.moviesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.moviesapp.controller.callback

class LogInFragment : Fragment(R.layout.login_fragment) {

    var callbackFragment:callback? = null

    override fun onAttach(context: Context) {
        var sharedPrefs = context.getSharedPreferences("users",Context.MODE_PRIVATE)
        var editor = sharedPrefs.edit()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment,container,false)
        var logInButton: Button = view.findViewById(R.id.logInButton)
        var signUpButton: Button = view.findViewById(R.id.signUpButton)
        var email: EditText = view.findViewById(R.id.email)
        var password: EditText = view.findViewById(R.id.password)

        logInButton.setOnClickListener{
            Log.d("LogIn","DA")
            if(email.text.toString() == "email" && password.text.toString() == "1234")
                openActivity(MainActivity::class.java)
        }

        signUpButton.setOnClickListener{
            callbackFragment?.changeFragment()
        }


        return view
    }
    private fun openActivity(nextActivity: Class<*>){
        val intent = Intent(activity!!.application,nextActivity)
        startActivity(intent)
        activity!!.finish()
    }

    fun getCallbackFragment(callbackFragment: callback){
        this.callbackFragment = callbackFragment
    }
}