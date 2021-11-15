package com.example.moviesapp.controller

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

open class SharedPrefsHandler {

    val PREF_EMAIL = "email"
    val PREF_LANGUAGE = "language"

    fun getSharedPrefs(ctx: Context): SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    open fun setEmail(ctx: Context, email: String){
        val editor = getSharedPrefs(ctx).edit()
        editor.putString(PREF_EMAIL,email)
        editor.commit()
    }
    open fun getEmail(ctx: Context): String{
        return getSharedPrefs(ctx).getString(PREF_EMAIL,"").toString()
    }

    open fun clearEmail(ctx: Context){
        val editor = getSharedPrefs(ctx).edit()
        editor.remove(PREF_EMAIL)
        editor.commit()
    }

    open fun setLanguage(ctx: Context, language: String){
        val editor = getSharedPrefs(ctx).edit()
        editor.putString(PREF_LANGUAGE,language)
        editor.commit()
    }
    open fun getLanguage(ctx: Context): String{
        return getSharedPrefs(ctx).getString(PREF_LANGUAGE,"").toString()
    }
}