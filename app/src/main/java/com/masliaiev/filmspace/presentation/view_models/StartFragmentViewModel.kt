package com.masliaiev.filmspace.presentation.view_models

import androidx.lifecycle.ViewModel
import com.masliaiev.filmspace.domain.usecases.GetAppModeUseCase
import javax.inject.Inject

class StartFragmentViewModel @Inject constructor(
    private val getAppModeUseCase: GetAppModeUseCase
) : ViewModel() {

    val appMode = getAppModeUseCase.getAppMode()

}