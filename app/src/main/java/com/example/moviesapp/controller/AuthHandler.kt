package com.example.moviesapp.controller

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.moviesapp.Activities.MainActivity
import com.example.moviesapp.data.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.StringBuilder
import java.security.MessageDigest
import kotlin.system.exitProcess

open class AuthHandler(auth: FirebaseAuth, context: Context): AppCompatActivity() {

    private val sharedPrefsHandler = SharedPrefsHandler()
    private var auth : FirebaseAuth
    private var context : Context
    private val activityOpener = ActivityOpener()

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

    fun registerUser(emailText: EditText, passwordText: EditText, confirmPasswordText : EditText,reqActivity: FragmentActivity){
        var email = emailText.text.toString()
        var password = passwordText.text.toString()
        var confirmPassword = confirmPasswordText.text.toString()

        if(!password.equals(confirmPassword)){
            confirmPasswordText.setError("Passwords not matching")
            confirmPasswordText.requestFocus()
            return
        }
        if(email.isEmpty()){
            emailText.setError("Email is requierd")
            emailText.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailText.setError("Invalid email")
            emailText.requestFocus()
            return
        }

        if(password.isEmpty()){
            passwordText.setError("Password is requierd")
            passwordText.requestFocus()
            return
        }
        if(password.length < 6){
            passwordText.setError("Min 6 characters")
            passwordText.requestFocus()
            return
        }


        auth.createUserWithEmailAndPassword(email,getMD5(password)).addOnCompleteListener(
            OnCompleteListener { task ->
                if(task.isSuccessful){
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    val user = User(email,getMD5(password))
                    FirebaseFirestore.getInstance().collection("users").add(user).addOnCompleteListener(
                        OnCompleteListener { task ->
                            if(task.isSuccessful){
                                Toast.makeText(context.applicationContext,"Successfully registered",Toast.LENGTH_SHORT).show()
                                sharedPrefsHandler.setEmail(context.applicationContext,email)
                                activityOpener.openActivity(reqActivity, MainActivity::class.java)
                            }
                            else{
                                Toast.makeText(context.applicationContext,"Not successfull",Toast.LENGTH_SHORT).show()
                            }
                        }).addOnFailureListener(OnFailureListener {
                            Log.d("Firestore failure",it.message.toString())
                    })
                }
                else{
                    Toast.makeText(context.applicationContext,"Not successfull",Toast.LENGTH_SHORT).show()
                }
            }).addOnFailureListener(OnFailureListener {
            Log.d("Auth Failure",it.message.toString())
        })
    }

    suspend fun userLogIn(emailText: String,passwordText: String,reqActivity: FragmentActivity,progressBar: ProgressBar){
        lifecycleScope.launch(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(emailText,passwordText)
            auth.signInWithEmailAndPassword(emailText, getMD5(passwordText)).addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            sharedPrefsHandler.setEmail(context.applicationContext, emailText)
                            activityOpener.openActivity(reqActivity,MainActivity::class.java)
                        } else {
                            Toast.makeText(context.applicationContext, "Unsuccessfully", Toast.LENGTH_SHORT).show()
                        }
                    })
            withContext(Dispatchers.Main){
                progressBar.visibility = View.VISIBLE
                delay(1000L)
                progressBar.visibility = View.GONE
            }
        }
    }
}