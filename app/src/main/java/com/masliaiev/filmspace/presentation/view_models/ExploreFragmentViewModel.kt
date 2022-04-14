package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.Genre
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.GetGenresUseCase
import com.masliaiev.filmspace.domain.usecases.LoadAccountUseCase
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class ExploreFragmentViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    init {
        getGenres()
    }

    private var _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>>
        get() = _genres

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError


    private fun getGenres() {
        viewModelScope.launch {
            val genresResult = getGenresUseCase.getGenres()
            when (genresResult.first) {
                ResultParams.SUCCESS -> {
                    genresResult.second?.let {
                        _genres.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }




}