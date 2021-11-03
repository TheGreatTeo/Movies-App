package com.example.moviesapp.controller

import android.content.Context
import android.util.Patterns
import android.widget.EditText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest

open class AuthHandler(auth: FirebaseAuth, context: Context) {

    private var auth : FirebaseAuth
    private var context : Context

    init {
        this.auth = auth
        this.context = context
    }

    fun byteArrayToHexString(array: Array<Byte>) : String{
        var result = StringBuilder(array.size*2)

        for( byte in array){
            val toAppend = String.format("%2x",byte).replace(" ","0")
            result.append(toAppend).append("-")
        }
        result.setLength(result.length - 1)

        return result.toString()
    }

    fun getMD5(input: String): String{
        var result = ""
        try{
            val md = MessageDigest.getInstance("MD5")
            val messageDigest = md.digest(input.toByteArray()).toTypedArray()

            result = byteArrayToHexString(messageDigest)
        }catch (e: Exception){
            result = "${e.message}"
        }
        return result
    }

    fun registerUser(email: String, password: String, confirmPassword : String){
        if(!password.equals(confirmPassword)){
            return
        }

        if(password.isEmpty()){
            return
        }
        if(password.length < 6){
            return
        }
        if(email.isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return
        }
    }
}