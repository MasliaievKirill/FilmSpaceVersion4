package com.masliaiev.filmspace.di

import androidx.lifecycle.ViewModel
import com.masliaiev.filmspace.presentation.view_models.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationFragmentViewModel::class)
    fun bindAuthenticationFragmentViewModel(impl: AuthenticationFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WebAuthFragmentViewModel::class)
    fun bindWebAuthFragmentViewModel(impl: WebAuthFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainFragmentViewModel::class)
    fun bindMainFragmentViewModel(impl: MainFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    fun bindHomeFragmentViewModel(impl: HomeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StartFragmentViewModel::class)
    fun bindStartFragmentViewModel(impl: StartFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExploreFragmentViewModel::class)
    fun bindExploreFragmentViewModel(impl: ExploreFragmentViewModel): ViewModel


}