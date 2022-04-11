package com.masliaiev.filmspace.helpers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ResultParams : Parcelable {
    SUCCESS, ACCOUNT_ERROR, NOT_RESPONSE, NO_CONNECTION
}