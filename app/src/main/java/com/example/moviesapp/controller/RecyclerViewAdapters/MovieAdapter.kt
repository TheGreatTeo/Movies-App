package com.example.moviesapp.controller.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.Movie.MovieItem
import com.squareup.picasso.Picasso

class MovieAdapter(private val movieList: List<MovieItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movies, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMovie = movieList[position]
        holder.title.text = currentMovie.title
        holder.rating.text = currentMovie.rating.toString() + " ⭐ "
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + currentMovie.imageResource).placeholder(R.drawable.image_loading).error(R.drawable.noimage).into(holder.imageView)
    }

    override fun getItemCount() = movieList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
        val rating: TextView = itemView.findViewById(R.id.rating)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if(position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}