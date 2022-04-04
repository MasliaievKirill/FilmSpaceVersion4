package com.masliaiev.filmspace.data.network.models.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AccountDto(
    @SerializedName("avatar")
    @Expose
    val avatar: AvatarDto,
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("iso_639_1")
    @Expose
    val iso6391: String,
    @SerializedName("iso_3166_1")
    @Expose
    val iso31661: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("include_adult")
    @Expose
    val includeAdult: Boolean,
    @SerializedName("username")
    @Expose
    val username: String
)
