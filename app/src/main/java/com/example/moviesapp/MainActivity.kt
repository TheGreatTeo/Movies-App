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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav: BottomNavigationView = findViewById(R.id.bottomNav)
        switchFragment(homeFragment,null)

        nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> switchFragment(homeFragment,null)
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
            commit()
        }
    }

    override fun passData(fragment: Fragment,data: Int) {
        val bundle = Bundle()
        bundle.putInt("genrePosition",data)
        switchFragment(fragment,bundle)
    }
}