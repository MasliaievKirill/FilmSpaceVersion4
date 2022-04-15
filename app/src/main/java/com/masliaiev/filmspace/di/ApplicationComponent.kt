package com.masliaiev.filmspace.di

import android.app.Application
import com.masliaiev.filmspace.presentation.fragments.*
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(fragment: MainFragment)

    fun inject(fragment: AuthFragment)

    fun inject(fragment: WebAuthFragment)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: StartFragment)

    fun inject(fragment: ExploreFragment)

    fun inject(fragment: AccountFragment)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}