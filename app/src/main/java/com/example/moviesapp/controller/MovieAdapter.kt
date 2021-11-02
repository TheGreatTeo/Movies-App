package com.example.moviesapp.controller

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.MovieItem
import java.net.URL

class MovieAdapter(private val movieList: List<MovieItem>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movies, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMovie = movieList[position]

        holder.imageView.setImageResource(currentMovie.imageResource)
        holder.title.text = currentMovie.title
        holder.rating.text = "⭐"+currentMovie.rating.toString()
    }

    override fun getItemCount() = movieList.size


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val rating: TextView = itemView.findViewById(R.id.rating)
    }
}