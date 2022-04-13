package com.masliaiev.filmspace.helpers

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SignedInParams : Parcelable {
    SIGNED_IN, GUEST
}