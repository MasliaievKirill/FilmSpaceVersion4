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

    @Binds
    @IntoMap
    @ViewModelKey(AccountFragmentViewModel::class)
    fun bindAccountFragmentViewModel(impl: AccountFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieListFragmentViewModel::class)
    fun bindMovieListFragmentViewModel(impl: MovieListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchFragmentViewModel::class)
    fun bindSearchFragmentViewModel(impl: SearchFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailMovieFragmentViewModel::class)
    fun bindDetailMovieFragmentViewModel(impl: DetailMovieFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LibraryFragmentViewModel::class)
    fun bindLibraryFragmentViewModel(impl: LibraryFragmentViewModel): ViewModel


}