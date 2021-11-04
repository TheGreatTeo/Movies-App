package com.example.moviesapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.MovieAdapter
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.data.MovieItem
import com.example.moviesapp.fragments.DashboardFragment
import com.example.moviesapp.fragments.HomeFragment
import com.example.moviesapp.fragments.LibraryFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import java.io.IOException
import java.net.URI

class MainActivity : AppCompatActivity(),Communicator {

    val homeFragment = HomeFragment()
    val dashboardFragment = DashboardFragment()
    val libraryFragment = LibraryFragment()
    var nav: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        nav= findViewById(R.id.bottomNav)
        switchFragment(homeFragment,null)

        nav?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> supportFragmentManager.beginTransaction().apply { replace(R.id.mainActivity,homeFragment).commit() }
                R.id.dashboard -> switchFragment(dashboardFragment,null)
                R.id.library -> switchFragment(libraryFragment,null)
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
        if(fragments.get(fragments.size-1) == dashboardFragment || fragments.get(fragments.size-1) == libraryFragment) {
            supportFragmentManager.beginTransaction()
                .apply { replace(R.id.mainActivity, homeFragment).commit() }
            nav?.selectedItemId = R.id.home
        }
        else
            supportFragmentManager.popBackStack()

    }
}