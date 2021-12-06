package com.example.moviesapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.Callback
import com.example.moviesapp.controller.GoBack
import com.example.moviesapp.controller.ViewPagerAdapter.Adapter
import com.example.moviesapp.fragments.MainActivityFragments.DashboardFragment
import com.example.moviesapp.fragments.MainActivityFragments.HomeFragment
import com.example.moviesapp.fragments.MainActivityFragments.LibraryFragment
import com.example.moviesapp.fragments.MainActivityFragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(),Communicator,Callback {

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
        if(savedInstanceState == null)
            switchFragment(homeFragment,null)

        dashboardFragment.getCallback(this)
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

    override fun passGenre(fragment: Fragment, genreId: Int, genreName: String) {
        val bundle = Bundle()
        bundle.putInt("genreId",genreId)
        bundle.putString("genreName",genreName)
        switchFragment(fragment,bundle)
    }
    override fun passMovie(fragment: Fragment, id: Int){
        val bundle = Bundle()
        bundle.putInt("id",id)
        switchFragment(fragment,bundle)
    }

    override fun searchView(fragment: Fragment) {
        val bundle = Bundle()
        switchFragment(fragment,bundle)
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        if(fragments.get(fragments.size-1) is DashboardFragment || fragments.get(fragments.size-1) is LibraryFragment || fragments.get(fragments.size-1) is SettingsFragment) {
            supportFragmentManager.beginTransaction()
                .apply { replace(R.id.mainActivity, homeFragment).commit() }
            nav?.selectedItemId = R.id.home
        }
        else if(fragments.get(fragments.size-1) is HomeFragment){
            supportFragmentManager.popBackStack()
            super.onBackPressed()

        }
        else
            supportFragmentManager.popBackStack()

    }

    override fun changeFragment() {
        Log.d("Clicked","Click!4")
        supportFragmentManager.popBackStack()
    }
}