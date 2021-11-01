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

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val logInFragment = LogInFragment()
        val signUpFragment = SignUpFragment()

        switchToFragment(logInFragment)
    }
    private fun switchToFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn,fragment)
            commit()
        }
    }

    private fun openActivity(activity: Class<*>){
        val intent = Intent(this,activity)
        startActivity(intent)
        finish()
    }
}