package com.example.moviesapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.CoroutinesHomework
import com.example.moviesapp.LogInActivity
import com.example.moviesapp.R
import com.example.moviesapp.controller.ActivityOpener
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.data.GenreItem
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import java.io.IOException
import java.util.*

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    val sharedPrefsHandler = SharedPrefsHandler()
    val activityOpener = ActivityOpener()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        loadLanguage()
        val signOutButton: Button = view.findViewById(R.id.signOut)
        val toCoroutine: Button = view.findViewById(R.id.toCoroutine)
        val changeLanguage: Button = view.findViewById(R.id.changeLanguage)

        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sharedPrefsHandler.clearEmail(requireContext().applicationContext)
            activityOpener.openActivity(requireContext().applicationContext,LogInActivity::class.java)
            requireActivity().finish()
        }

        toCoroutine.setOnClickListener {
            activityOpener.openActivity(requireContext().applicationContext,CoroutinesHomework::class.java)
        }

        changeLanguage.setOnClickListener {
            val languages = arrayOf("English","Romanian")
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setTitle(R.string.changeLanguageTitle)
            alertDialog.setSingleChoiceItems(languages,-1,DialogInterface.OnClickListener { dialog, which ->
                if(which == 0) {
                    setLanguage("en")
                    recreate(requireActivity())
                }
                if(which == 1) {
                    setLanguage("ro")
                    recreate(requireActivity())
                }
                dialog.dismiss()
            })
            alertDialog.create().show()
        }


        return view
    }

    fun setLanguage(language: String){
        var locales = Locale(language)
        Locale.setDefault(locales)
        var config = Configuration()
        config.locale = locales
        requireContext().resources.updateConfiguration(config,requireContext().resources.displayMetrics)
        sharedPrefsHandler.setLanguage(requireContext(),language)
    }

    fun loadLanguage(){
        setLanguage(sharedPrefsHandler.getLanguage(requireContext()))
    }
}