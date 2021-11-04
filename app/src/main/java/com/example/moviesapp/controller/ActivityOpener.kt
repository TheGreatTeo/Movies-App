package com.example.moviesapp.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

open class ActivityOpener:AppCompatActivity() {
    open fun openActivity(context: Context,nextActivity: Class<*>){
        val intent = Intent(context.applicationContext,nextActivity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        finishActivity(1234)
        context.startActivity(intent)
    }
}