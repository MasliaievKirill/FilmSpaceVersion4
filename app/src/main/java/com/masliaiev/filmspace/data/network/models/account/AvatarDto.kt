package com.masliaiev.filmspace.data.network.models.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AvatarDto(
    @SerializedName("gravatar")
    @Expose
    val gravatar: GravatarDto
)
