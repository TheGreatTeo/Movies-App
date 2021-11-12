package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class CoroutinesHomework : AppCompatActivity() {
    lateinit var job: Job
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_New)
        setContentView(R.layout.activity_coroutines_homework)

        val stopCoroutine: Button = findViewById(R.id.stopCoroutine)

        job = lifecycleScope.launch {
            try{
                doWork()
            }catch (e: CancellationException){
                Log.d("Test","Work canceled")
            }finally {
                cleanUp()
            }
        }

        stopCoroutine.setOnClickListener {
            job.cancel()
        }

    }

    suspend fun doWork(){
        return withContext(Dispatchers.Default){
            for(i in 0 .. 5){
                Log.d("Test",i.toString())
                Thread.sleep(1000)
            }
        }
    }
    suspend fun cleanUp(){
        return withContext(NonCancellable){
            Log.d("Test","Clean up")
        }
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }
}