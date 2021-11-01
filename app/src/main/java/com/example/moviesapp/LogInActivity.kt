package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val logInFragment = LogInFragment()
        val signUpFragment = SignUpFragment()
        val toSignUp : Button = findViewById(R.id.toSignUp)
        val toLogIn : Button = findViewById(R.id.toLogIn)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn,logInFragment)
            commit()
        }

        toSignUp.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLogIn,signUpFragment)
                commit()
            }
        }

        toLogIn.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLogIn,logInFragment)
                commit()
            }
        }
    }
}