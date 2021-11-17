package com.example.moviesapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.fragments.MainActivityFragments.DashboardFragment
import com.example.moviesapp.fragments.MainActivityFragments.HomeFragment
import com.example.moviesapp.fragments.MainActivityFragments.LibraryFragment
import com.example.moviesapp.fragments.MainActivityFragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),Communicator {

    val homeFragment = HomeFragment()
    val dashboardFragment = DashboardFragment()
    val libraryFragment = LibraryFragment()
    val settingsFragment = SettingsFragment()
    var nav: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_New)
        setContentView(R.layout.activity_main)


        nav= findViewById(R.id.bottomNav)
        switchFragment(homeFragment,null)

        nav?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> supportFragmentManager.beginTransaction().apply { replace(R.id.mainActivity,homeFragment).commit() }
                R.id.dashboard -> switchFragment(dashboardFragment,null)
                R.id.library -> switchFragment(libraryFragment,null)
                R.id.settings -> switchFragment(settingsFragment,null)
            }
            true
        }

    }

    private fun switchFragment(fragment: Fragment,bundle: Bundle?){
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainActivity,fragment)
            addToBackStack("home")
            commit()
        }
    }

    override fun passData(fragment: Fragment,data: Int) {
        val bundle = Bundle()
        bundle.putInt("genrePosition",data)
        switchFragment(fragment,bundle)
    }
    override fun passMovie(fragment: Fragment,genre:Int, movie:Int){
        val bundle = Bundle()
        bundle.putInt("genrePosition",genre)
        bundle.putInt("moviePosition",movie)
        switchFragment(fragment,bundle)
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        if(fragments.get(fragments.size-1) == dashboardFragment || fragments.get(fragments.size-1) == libraryFragment || fragments.get(fragments.size-1) == settingsFragment) {
            supportFragmentManager.beginTransaction()
                .apply { replace(R.id.mainActivity, homeFragment).commit() }
            nav?.selectedItemId = R.id.home
        }
        else if(fragments.get(fragments.size-1) == homeFragment){
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(startMain)
        }
        else
            supportFragmentManager.popBackStack()

    }
}