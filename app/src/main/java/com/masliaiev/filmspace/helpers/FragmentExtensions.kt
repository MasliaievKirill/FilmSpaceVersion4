package com.masliaiev.filmspace.helpers

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.masliaiev.filmspace.R
import java.lang.RuntimeException

fun Fragment.findTopNavController(): NavController{
    val topLevelHost =
        requireActivity().supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment
    return topLevelHost.navController
}