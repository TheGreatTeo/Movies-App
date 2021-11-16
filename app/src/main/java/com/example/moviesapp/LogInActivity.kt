package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesapp.controller.callback
import com.example.moviesapp.fragments.LogInFragment
import com.example.moviesapp.fragments.SignUpFragment
import android.content.Intent
import androidx.fragment.app.Fragment


class LogInActivity : AppCompatActivity(), callback {

    var signUpFragment = SignUpFragment()
    var logInFragment = LogInFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_New)
        setContentView(R.layout.activity_log_in)

        /*
        if(savedInstanceState != null){
            if(supportFragmentManager.getFragment(savedInstanceState,"fragment") == logInFragment) {
                switchFragment(logInFragment)
            }
            else if(supportFragmentManager.getFragment(savedInstanceState,"fragment") == signUpFragment) {
                switchFragment(signUpFragment)
            }
        }
        else
            switchToLogIn()
         */
        switchToLogIn()
    }

    private fun switchToLogIn() {
        logInFragment.getCallbackFragment(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn, logInFragment)
            addToBackStack(null)
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

    private fun switchFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn, fragment)
            commit()
        }
    }

    override fun changeFragment() {
        switchToSignUp()
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        if (fragments.get(fragments.size - 1) == logInFragment) {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
        else
            if (fragments.get(fragments.size - 1) == signUpFragment){
                switchToLogIn()
        }

    }

    /*
    override fun onSaveInstanceState(outState: Bundle) {
        val fragments = supportFragmentManager.fragments
        supportFragmentManager.putFragment(outState,"fragment",fragments.get(fragments.size-1))
        super.onSaveInstanceState(outState)
    }
     */
}