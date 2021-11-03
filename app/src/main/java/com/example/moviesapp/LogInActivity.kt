package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesapp.controller.callback
import com.example.moviesapp.fragments.LogInFragment
import com.example.moviesapp.fragments.SignUpFragment

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