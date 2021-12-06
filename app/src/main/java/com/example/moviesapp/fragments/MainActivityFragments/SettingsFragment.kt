package com.example.moviesapp.fragments.MainActivityFragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat.recreate
import com.example.moviesapp.Activities.CoroutinesHomework
import com.example.moviesapp.Activities.LogInActivity
import com.example.moviesapp.R
import com.example.moviesapp.controller.ActivityOpener
import com.example.moviesapp.controller.SharedPrefsHandler
import com.google.firebase.auth.FirebaseAuth
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
        val email: TextView = view.findViewById(R.id.email)
        val username: TextView = view.findViewById(R.id.username)
        val accountDetails: Button = view.findViewById(R.id.accountDetails)
        val signOutButton: Button = view.findViewById(R.id.signOut)
        val changeLanguage: Button = view.findViewById(R.id.changeLanguage)

        email.text = sharedPrefsHandler.getEmail(this.requireContext())
        username.text = sharedPrefsHandler.getUsername(this.requireContext())
        changeLanguage.setBackgroundColor(resources.getColor(R.color.cardColor))
        signOutButton.setBackgroundColor(resources.getColor(R.color.cardColor))
        accountDetails.setBackgroundColor(resources.getColor(R.color.cardColor))

        signOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sharedPrefsHandler.clearEmail(requireContext().applicationContext)
            sharedPrefsHandler.clearGenresIdName(requireContext().applicationContext)
            activityOpener.openActivity(requireActivity(), LogInActivity::class.java)
            requireActivity().finish()
        }

        changeLanguage.setOnClickListener {
            val languages = arrayOf("English","Romanian")
            val alertDialog = AlertDialog.Builder(requireActivity(),R.style.MyDialogTheme)
            alertDialog.setTitle(R.string.changeLanguageTitle)
            var checkedItem = -1
            when(sharedPrefsHandler.getLanguage(this.requireContext())){
                "en" -> checkedItem = 0
                "ro" -> checkedItem = 1
            }
            alertDialog.setSingleChoiceItems(languages,checkedItem,DialogInterface.OnClickListener { dialog, which ->
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