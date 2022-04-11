package com.masliaiev.filmspace.domain.usecases

import com.masliaiev.filmspace.domain.entity.Account
import com.masliaiev.filmspace.domain.repository.AppRepository
import javax.inject.Inject

class GetAccountDetailsUseCase @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getAccountDetails(): Account{
        return repository.getAccountDetails()
    }
}