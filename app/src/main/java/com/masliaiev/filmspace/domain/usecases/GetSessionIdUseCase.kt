package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class GetSessionIdUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun getSessionId(): String {
        return repository.getSessionId()
    }
}