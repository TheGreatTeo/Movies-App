package com.example.moviesapp.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity

open class ActivityOpener:AppCompatActivity() {
    open fun openActivity(reqActivity: FragmentActivity,nextActivity: Class<*>){
        val intent = Intent(reqActivity.applicationContext,nextActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        reqActivity.startActivity(intent)
        reqActivity.finish()
    }
}