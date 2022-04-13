package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masliaiev.filmspace.domain.entity.responses.CreateSessionResponse
import com.masliaiev.filmspace.domain.usecases.CreateSessionUseCase
import com.masliaiev.filmspace.domain.usecases.SetAppModeUseCase
import com.masliaiev.filmspace.domain.usecases.SetSessionIdUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class WebAuthFragmentViewModel @Inject constructor(
    private val createSessionUseCase: CreateSessionUseCase,
    private val setAppModeUseCase: SetAppModeUseCase,
    private val setSessionIdUseCase: SetSessionIdUseCase
) : ViewModel() {

    private var _createSessionResponse = MutableLiveData<CreateSessionResponse>()
    val createSessionResponse: LiveData<CreateSessionResponse>
        get() = _createSessionResponse


    fun createSession(requestToken: String) {
        viewModelScope.launch {
            val createSessionResponse = createSessionUseCase.createSession(requestToken)
            _createSessionResponse.value = createSessionResponse
        }
    }

    fun setAppMode(appMode: String){
        setAppModeUseCase.setAppMode(appMode)
    }

    fun setSessionId(sessionId: String){
        setSessionIdUseCase.setSessionId(sessionId)
    }


}