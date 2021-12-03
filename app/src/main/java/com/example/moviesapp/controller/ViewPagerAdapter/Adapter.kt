package com.example.moviesapp.controller.ViewPagerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.moviesapp.R
import com.example.moviesapp.data.Movie.MovieItem
import com.squareup.picasso.Picasso

class Adapter(movieList: List<MovieItem>, context: Context, private val listener: OnItemClickListener): PagerAdapter() {

    private var movieList: List<MovieItem> = listOf()
    private var context: Context? = null

    init {
        this.movieList = movieList
        this.context = context
    }

    override fun getCount(): Int = movieList.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_pager_item_movies,container,false)
        val currentMovie = movieList.get(position)
        val imageView: ImageView = view.findViewById(R.id.image)
        val title: TextView = view.findViewById(R.id.title)
        //val rating: TextView = view.findViewById(R.id.rating)

        Picasso.get().load("https://image.tmdb.org/t/p/w500" + currentMovie.imageResource).placeholder(R.drawable.image_loading).error(R.drawable.noimage).into(imageView)
        title.setText(currentMovie.title)
        //rating.text = "" + currentMovie.rating + " ⭐ "

        view.setOnClickListener {
            listener.onItemClick(currentMovie.id)
        }

        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}