package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.responses.CreateRequestTokenResponse
import com.masliaiev.filmspace.domain.usecases.CreateRequestTokenUseCase
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthenticationFragmentViewModel @Inject constructor(
    private val createRequestTokenUseCase: CreateRequestTokenUseCase
) : ViewModel() {

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError

    private var _requestToken = MutableLiveData<CreateRequestTokenResponse>()
    val requestTokenResponse: LiveData<CreateRequestTokenResponse>
        get() = _requestToken


    fun createRequestToken() {
        viewModelScope.launch {
            val requestTokenResult = createRequestTokenUseCase.createRequestToken()
            when (requestTokenResult.first) {
                ResultParams.SUCCESS -> {
                    requestTokenResult.second?.let {
                        _requestToken.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }


}