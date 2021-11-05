package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesapp.controller.callback
import com.example.moviesapp.fragments.LogInFragment
import com.example.moviesapp.fragments.SignUpFragment
import android.content.Intent




class LogInActivity : AppCompatActivity(), callback {

    var signUpFragment = SignUpFragment()
    var logInFragment = LogInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        switchToLogIn()
    }

    private fun switchToLogIn() {
        logInFragment.getCallbackFragment(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn, logInFragment)
            commit()
        }
    }

    private fun switchToSignUp() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn, signUpFragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun changeFragment() {
        switchToSignUp()
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        if (fragments.get(fragments.size - 1) != signUpFragment) {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
        else{
            super.onBackPressed()
        }
    }
}