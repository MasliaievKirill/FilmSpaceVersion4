package com.masliaiev.filmspace.di

import android.app.Application
import com.masliaiev.filmspace.presentation.activity.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}