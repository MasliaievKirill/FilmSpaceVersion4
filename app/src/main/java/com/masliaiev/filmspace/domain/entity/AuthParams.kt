package com.masliaiev.filmspace.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class AuthParams: Parcelable {
    ALLOW, DENY, GUEST
}