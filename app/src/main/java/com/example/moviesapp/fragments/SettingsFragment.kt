package com.example.moviesapp.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.LogInActivity
import com.example.moviesapp.R
import com.example.moviesapp.controller.ActivityOpener
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.data.GenreItem
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import java.io.IOException

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    val sharedPrefsHandler = SharedPrefsHandler()
    val activityOpener = ActivityOpener()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val signOutButton: Button = view.findViewById(R.id.signOut)

        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sharedPrefsHandler.clearEmail(context!!.applicationContext)
            activityOpener.openActivity(context!!.applicationContext,LogInActivity::class.java)
            activity!!.finish()
        }
        return view
    }
}