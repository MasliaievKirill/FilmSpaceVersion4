package com.masliaiev.filmspace.di

import android.app.Application
import com.masliaiev.filmspace.presentation.fragments.AuthFragment
import com.masliaiev.filmspace.presentation.fragments.HomeFragment
import com.masliaiev.filmspace.presentation.fragments.MainFragment
import com.masliaiev.filmspace.presentation.fragments.WebAuthFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(fragment: MainFragment)

    fun inject(fragment: AuthFragment)

    fun inject(fragment: WebAuthFragment)

    fun inject(fragment: HomeFragment)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}