package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class CheckShowGoogleReviewUseCase @Inject constructor(
    private val repository: AppRepository
) {
    fun checkShowGoogleReview(): Boolean {
        return repository.checkShowGoogleReview()
    }
}