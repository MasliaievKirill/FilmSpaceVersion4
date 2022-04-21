package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.Account
import com.masliaiev.filmspace.domain.usecases.*
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountFragmentViewModel @Inject constructor(
    private val loadAccountUseCase: LoadAccountUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase,
    private val getAppModeUseCase: GetAppModeUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase,
    private val setAppModeUseCase: SetAppModeUseCase,
    private val setSessionIdUseCase: SetSessionIdUseCase
) : ViewModel() {

    init {
        loadAccount()
    }

    val appMode = getAppModeUseCase.getAppMode()

    private var _account = MutableLiveData<Account>()
    val account: LiveData<Account>
        get() = _account

    private var _deleteSessionSuccess = MutableLiveData<Boolean>()
    val deleteSessionSuccess: LiveData<Boolean>
        get() = _deleteSessionSuccess

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean>
        get() = _error

    private var _apiError = MutableLiveData<Boolean>()
    val apiError: LiveData<Boolean>
        get() = _apiError


    private fun loadAccount() {
        viewModelScope.launch {
            when (loadAccountUseCase.loadAccountDetails(getSessionIdUseCase.getSessionId())) {
                ResultParams.SUCCESS -> {
                    _account.value = getAccountDetailsUseCase.getAccountDetails()
                }
                ResultParams.ACCOUNT_ERROR -> _apiError.value = true
                ResultParams.NO_CONNECTION -> _error.value = true
                ResultParams.NOT_RESPONSE -> _error.value = true
            }
        }
    }

    fun deleteSession() {
        viewModelScope.launch {
            val result = deleteSessionUseCase.deleteSession(getSessionIdUseCase.getSessionId())
            when (result.first) {
                ResultParams.SUCCESS -> {
                    result.second?.let {
                        _deleteSessionSuccess.value = it
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


}