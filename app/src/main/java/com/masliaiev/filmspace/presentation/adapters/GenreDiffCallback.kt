package com.masliaiev.filmspace.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.masliaiev.filmspace.domain.entity.Genre

class GenreDiffCallback: DiffUtil.ItemCallback<Genre>() {

    override fun areItemsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Genre, newItem: Genre): Boolean {
        return oldItem == newItem
    }
}