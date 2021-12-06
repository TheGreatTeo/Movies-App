package com.example.moviesapp.controller.ViewPagerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.moviesapp.R
import com.example.moviesapp.data.Genre.Genre

class MovieGenreAdapter(genreList: List<Genre>, context: Context): PagerAdapter() {

    private var genreList: List<Genre> = listOf()
    private var context: Context? = null

    init {
        this.genreList = genreList
        this.context = context
    }

    override fun getCount(): Int = genreList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_pager_item_genre,container,false)

        val genreName: TextView = view.findViewById(R.id.genre)
        genreName.text = genreList.get(position).genreName

        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}