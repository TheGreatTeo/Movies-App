package com.example.moviesapp.controller.RecyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.Genre.GenreItem

class GenreAdapter(private val genreList: List<GenreItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.genres, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentGenre = genreList[position]

        holder.genreImage.setImageResource(currentGenre.imageResource)
        holder.genre.text = currentGenre.genre
    }

    override fun getItemCount() = genreList.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val genreImage: ImageView = itemView.findViewById(R.id.genreImage)
        val genre: TextView = itemView.findViewById(R.id.genre)

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