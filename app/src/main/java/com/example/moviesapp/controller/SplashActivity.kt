package com.example.moviesapp.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesapp.LogInActivity
import com.example.moviesapp.MainActivity
import com.example.moviesapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.moviesapp.controller.ActivityOpener
import java.lang.Thread.sleep

class SplashActivity : AppCompatActivity() {
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val activityOpener = ActivityOpener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if(firebaseUser != null){
            activityOpener.openActivity(this,MainActivity::class.java)

        }
        else{
            setContentView(R.layout.activity_splash)
            var timer = Thread(){
                kotlin.run {
                    try {
                        sleep(500)
                    }catch (e: InterruptedException){}
                    finally {
                        activityOpener.openActivity(this,LogInActivity::class.java)

                    }
                }
            }
            timer.start()
        }
    }
}