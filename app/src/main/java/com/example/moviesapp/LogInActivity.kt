package com.example.moviesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.moviesapp.controller.callback

class LogInActivity : AppCompatActivity(), callback {

    var signUpFragment = SignUpFragment()
    var logInFragment = LogInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        switchToLogIn()
    }
    private fun switchToLogIn(){
        logInFragment.getCallbackFragment(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn,logInFragment)
            commit()
        }
    }
    private fun switchToSignUp(){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn,signUpFragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun changeFragment() {
        switchToSignUp()
    }
}