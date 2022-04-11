package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.AccountStatus
import com.masliaiev.filmspace.domain.usecases.LoadAccountUseCase
import com.masliaiev.filmspace.helpers.ResultParams
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragmentViewModel @Inject constructor(
    private val loadAccountUseCase: LoadAccountUseCase
) : ViewModel() {

    private var _accountIsActive = MutableLiveData<Boolean>()
    val accountIsActive: LiveData<Boolean>
        get() = _accountIsActive


    fun loadAccount(sessionId: String) {
        viewModelScope.launch {
            val accountStatus = loadAccountUseCase.loadAccountDetails(sessionId)
            if (accountStatus == ResultParams.SUCCESS) {
                _accountIsActive.value = true
            } else if (accountStatus == ResultParams.ACCOUNT_ERROR) {
                _accountIsActive.value = false
            }
        }
    }




}