package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class GetAppModeUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun getAppMode(): String {
        return repository.getAppMode()
    }
}