package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class SetAppModeUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun setAppMode(appMode: String) {
        repository.setAppMode(appMode)
    }
}