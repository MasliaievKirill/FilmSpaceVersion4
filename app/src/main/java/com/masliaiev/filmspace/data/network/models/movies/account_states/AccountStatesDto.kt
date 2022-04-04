package com.masliaiev.filmspace.data.network.models.movies.account_states

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccountStatesDto(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("favorite")
    @Expose
    val favorite: Boolean,
    @SerializedName("rated")
    @Expose
    val rated: RatedDto?,
    @SerializedName("watchlist")
    @Expose
    val watchlist: Boolean

)
