package com.masliaiev.filmspace.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.data.database.AppDao
import com.masliaiev.filmspace.data.database.AppDatabase
import com.masliaiev.filmspace.data.network.ApiFactory
import com.masliaiev.filmspace.data.network.ApiService
import com.masliaiev.filmspace.data.repository.AppRepositoryImpl
import com.masliaiev.filmspace.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindAppRepository(impl: AppRepositoryImpl): AppRepository

    companion object{

        @Provides
        @ApplicationScope
        fun provideAppDao(application: Application): AppDao {
            return AppDatabase.getInstance(application).appDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @Provides
        fun provideSharedPreferences(application: Application): SharedPreferences {
            return application.getSharedPreferences(AppConstants.APP_PREFERENCES, Context.MODE_PRIVATE)
        }
    }
}