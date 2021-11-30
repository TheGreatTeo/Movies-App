package com.example.moviesapp.controller.ViewPagerAdapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.moviesapp.R
import com.example.moviesapp.data.Cast
import com.example.moviesapp.data.CastMember
import com.example.moviesapp.data.Genre
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class CastAdapter(castMembers: List<CastMember>, context: Context): PagerAdapter() {

    private var castMembers: List<CastMember> = listOf()
    private var context: Context? = null

    init {
        this.castMembers = castMembers
        this.context = context
    }

    override fun getCount(): Int = castMembers.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.view_pager_item_cast,container,false)

        val castName: TextView = view.findViewById(R.id.castName)
        val castImage: ShapeableImageView = view.findViewById(R.id.castImage)
        castName.text = castMembers.get(position).name
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+castMembers.get(position).profile).error(R.drawable.noimage).into(castImage)

        container.addView(view,0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

}