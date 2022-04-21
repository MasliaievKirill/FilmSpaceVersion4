package com.masliaiev.filmspace.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.MovieItemInMovieListBinding
import com.masliaiev.filmspace.domain.entity.Movie
import com.squareup.picasso.Picasso

class MoviePagerAdapter: PagingDataAdapter<Movie, MovieInListViewHolder>(MovieDiffCallback()) {

    var onMovieClickListener: OnMovieClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieInListViewHolder {
        val binding = MovieItemInMovieListBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return MovieInListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieInListViewHolder, position: Int) {
        val movie = getItem(position)
        movie?.let {
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
}