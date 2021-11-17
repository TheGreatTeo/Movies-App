package com.example.moviesapp.Activities

import android.Manifest
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesapp.controller.callback
import com.example.moviesapp.fragments.LogInActivityFragments.LogInFragment
import com.example.moviesapp.fragments.LogInActivityFragments.SignUpFragment
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.moviesapp.R


class LogInActivity : AppCompatActivity(), callback {

    var signUpFragment = SignUpFragment()
    var logInFragment = LogInFragment()
    val LOCATION_PERMISSION_CODE  = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_New)
        setContentView(R.layout.activity_log_in)

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Log.d("Permission","Is granted")
        }
        else{
            requestLocationPermission()
        }

        if(savedInstanceState == null)
            switchToLogIn()
        else{
            val fragments = supportFragmentManager.fragments
            if(fragments.get(fragments.size-1) == logInFragment)
                switchToLogIn()
            else
                if(fragments.get(fragments.size-1) == signUpFragment)
                    switchToSignUp()
        }
    }

    private fun switchToLogIn() {
        logInFragment.getCallbackFragment(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn, logInFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun switchToSignUp() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn, signUpFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun switchFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLogIn, fragment)
            commit()
        }
    }

    override fun changeFragment() {
        switchToSignUp()
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        if (fragments.get(fragments.size - 1) == logInFragment) {
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
        else
            if (fragments.get(fragments.size - 1) == signUpFragment){
                switchToLogIn()
        }

    }
    override fun onSaveInstanceState(outState: Bundle) {
        val fragments = supportFragmentManager.fragments
        supportFragmentManager.putFragment(outState,"fragment",fragments.get(fragments.size-1))
        super.onSaveInstanceState(outState)
    }

    fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            val builder = AlertDialog.Builder(this)
                    .setTitle("Location permission")
                    .setMessage("This permission is needed!")
                    .setPositiveButton("Ok",DialogInterface.OnClickListener { dialog, which ->
                        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_PERMISSION_CODE)
                    })
                    .setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, which ->  dialog.dismiss() })
                    .create().show()
        }else{
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),LOCATION_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LOCATION_PERMISSION_CODE){
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission is granted!",Toast.LENGTH_LONG)
            }else{
                Toast.makeText(this,"Permission denied!",Toast.LENGTH_LONG)
            }
        }
    }

}