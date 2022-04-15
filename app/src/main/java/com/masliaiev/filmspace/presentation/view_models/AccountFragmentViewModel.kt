package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.Account
import com.masliaiev.filmspace.domain.entity.Genre
import com.masliaiev.filmspace.domain.entity.Movie
import com.masliaiev.filmspace.domain.usecases.GetAccountDetailsUseCase
import com.masliaiev.filmspace.domain.usecases.GetGenresUseCase
import com.masliaiev.filmspace.domain.usecases.GetSessionIdUseCase
import com.masliaiev.filmspace.domain.usecases.LoadAccountUseCase
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountFragmentViewModel @Inject constructor(
    private val loadAccountUseCase: LoadAccountUseCase,
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val getSessionIdUseCase: GetSessionIdUseCase
) : ViewModel() {

    init {
        loadAccount()
    }

    private var _account = MutableLiveData<Account>()
    val account: LiveData<Account>
        get() = _account

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




}