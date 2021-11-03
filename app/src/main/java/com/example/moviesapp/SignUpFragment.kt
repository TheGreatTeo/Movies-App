package com.example.moviesapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class SignUpFragment : Fragment(R.layout.signup_fragment) {


    override fun onAttach(context: Context) {
        var sharedPrefs = context.getSharedPreferences("users", Context.MODE_PRIVATE)
        var editor = sharedPrefs.edit()
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        return view
    }
}