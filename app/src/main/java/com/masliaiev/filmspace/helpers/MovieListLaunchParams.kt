package com.masliaiev.filmspace.helpers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class MovieListLaunchParams : Parcelable {
    TOP_RATED, POPULAR, NOW_PLAYING, UPCOMING, TRENDING_TODAY, TRENDING_WEEK, WATCHLIST, RATED, FAVOURITE, GENRE
}