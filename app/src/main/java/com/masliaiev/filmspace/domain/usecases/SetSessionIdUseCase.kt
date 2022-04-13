package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class SetSessionIdUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun setSessionId(sessionId: String) {
        repository.setSessionId(sessionId)
    }
}