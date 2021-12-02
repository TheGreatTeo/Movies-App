package com.example.moviesapp.controller

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

open class SharedPrefsHandler {

    val PREF_EMAIL = "email"
    val PREF_USERNAME = "username"
    val PREF_LANGUAGE = "language"
    val PREF_GENRESID = "genresId"
    val PREF_GENRESNAME = "genresName"

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

    open fun setUsername(ctx: Context, username: String){
        val editor = getSharedPrefs(ctx).edit()
        editor.putString(PREF_USERNAME,username)
        editor.commit()
    }
    open fun getUsername(ctx: Context): String{
        return getSharedPrefs(ctx).getString(PREF_USERNAME,"").toString()
    }

    open fun clearUsername(ctx: Context){
        val editor = getSharedPrefs(ctx).edit()
        editor.remove(PREF_USERNAME)
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

    open fun setGenres(ctx: Context,genresId: MutableList<String>,genresName: MutableList<String>){
        val editor = getSharedPrefs(ctx).edit()
        editor.putStringList(PREF_GENRESID,genresId)
        editor.putStringList(PREF_GENRESNAME,genresName)
        editor.commit()
    }
    open fun getGenresId(ctx: Context): MutableList<String>{
        return getSharedPrefs(ctx).getStringList(PREF_GENRESID)
    }
    open fun getGenresName(ctx: Context): MutableList<String>{
        return getSharedPrefs(ctx).getStringList(PREF_GENRESNAME)
    }
    open fun clearGenresIdName(ctx: Context){
        val editor = getSharedPrefs(ctx).edit()
        editor.remove(PREF_GENRESID)
        editor.remove(PREF_GENRESNAME)
        editor.commit()
    }
    fun SharedPreferences.Editor.putStringList(key: String, list: MutableList<String>){
        this.putString(key,list.joinToString(";"))
    }
    fun SharedPreferences.getStringList(key: String): MutableList<String>{
        if(((this.getString(key,"")?.split(";") ?: mutableListOf()) as MutableList<String>).isEmpty())
            return mutableListOf()
        else
            return (this.getString(key,"")?.split(";") ?: mutableListOf()) as MutableList<String>
    }
}