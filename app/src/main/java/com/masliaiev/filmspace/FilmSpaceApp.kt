package com.masliaiev.filmspace

import android.app.Application
import com.masliaiev.filmspace.di.DaggerApplicationComponent

class FilmSpaceApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}