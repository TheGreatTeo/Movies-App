package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class LogInActivity : AppCompatActivity() {

    var fragment : Fragment? = null
    var fragmentManager : FragmentManager? = null
    var fragmentTransaction : FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        fun addFragment(){
            fragment = LogInFragment()
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager!!.beginTransaction()
        }
    }
}