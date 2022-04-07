package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var navController: NavController

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        if (sharedPreferences.getString(KEY_APP_MODE, UNKNOWN_MODE) == UNKNOWN_MODE){
            findNavController().navigate(
                R.id.action_main_fragment_to_authentication_fragment
            )

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{

        const val APP_PREFERENCES = "film_space_app"
        const val KEY_APP_MODE = "app_mode"

        const val UNKNOWN_MODE = "unknown"
        const val ACCOUNT_MODE = "account"
        const val GUEST_MODE = "guest"
    }
}