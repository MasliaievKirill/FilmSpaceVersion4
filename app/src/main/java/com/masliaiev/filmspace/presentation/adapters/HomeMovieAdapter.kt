package com.masliaiev.filmspace.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.MovieItemBinding
import com.masliaiev.filmspace.domain.entity.Movie
import com.squareup.picasso.Picasso

class HomeMovieAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffCallback()) {

    var onMovieClickListener: OnMovieClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        Picasso.get().load(movie.posterPath).placeholder(R.drawable.poster_placeholder)
            .into(holder.binding.ivPoster)
        holder.binding.tvTitle.text = movie.title
        holder.binding.tvYear.text = movie.releaseDate
        holder.binding.tvRating.text = movie.voteAverage
        holder.binding.root.setOnClickListener {
            onMovieClickListener?.onMovieClick(movie.id)
        }
    }

}