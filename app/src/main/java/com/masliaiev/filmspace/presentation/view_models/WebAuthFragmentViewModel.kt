package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.responses.CreateSessionResponse
import com.masliaiev.filmspace.domain.usecases.CreateSessionUseCase
import com.masliaiev.filmspace.domain.usecases.LoadAccountUseCase
import com.masliaiev.filmspace.domain.usecases.SetAppModeUseCase
import com.masliaiev.filmspace.domain.usecases.SetSessionIdUseCase
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebAuthFragmentViewModel @Inject constructor(
    private val createSessionUseCase: CreateSessionUseCase,
    private val setAppModeUseCase: SetAppModeUseCase,
    private val setSessionIdUseCase: SetSessionIdUseCase,
    private val loadAccountUseCase: LoadAccountUseCase
) : ViewModel() {

    private var _createSessionResponse = MutableLiveData<CreateSessionResponse>()
    val createSessionResponse: LiveData<CreateSessionResponse>
        get() = _createSessionResponse

    private var _loadAccountSuccess = MutableLiveData<Boolean>()
    val loadAccountSuccess: LiveData<Boolean>
        get() = _loadAccountSuccess

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError


    fun createSession(requestToken: String) {
        viewModelScope.launch {
            val createSessionResponse = createSessionUseCase.createSession(requestToken)
            when (createSessionResponse.first) {
                ResultParams.SUCCESS -> {
                    createSessionResponse.second?.let {
                        _createSessionResponse.value = it
                    }
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    fun setAppMode(appMode: String) {
        setAppModeUseCase.setAppMode(appMode)
    }

    fun setSessionId(sessionId: String) {
        setSessionIdUseCase.setSessionId(sessionId)
    }


    fun loadAccount(sessionId: String) {
        viewModelScope.launch {
            when (loadAccountUseCase.loadAccountDetails(sessionId)) {
                ResultParams.SUCCESS -> {
                    _loadAccountSuccess.value = true
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }


}