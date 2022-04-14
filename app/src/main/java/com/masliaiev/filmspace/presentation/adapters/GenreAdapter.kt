package com.masliaiev.filmspace.presentation.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.masliaiev.filmspace.databinding.GenreItemBinding
import com.masliaiev.filmspace.domain.entity.Genre
import kotlin.random.Random

class GenreAdapter : ListAdapter<Genre, GenreViewHolder>(GenreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = GenreItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = getItem(position)
        val colors = listOf(
            Color.YELLOW,
            Color.BLUE,
            Color.GRAY,
            Color.RED,
            Color.MAGENTA,
            Color.DKGRAY,
            Color.LTGRAY
        )
        val pos = Random.nextInt(colors.size)
        holder.binding.clGenre.setBackgroundColor(colors[pos])
        holder.binding.tvGenreName.text = genre.name
    }
}