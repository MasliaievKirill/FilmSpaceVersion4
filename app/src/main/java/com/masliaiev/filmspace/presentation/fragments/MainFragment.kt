package com.masliaiev.filmspace.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentMainBinding
import com.masliaiev.filmspace.helpers.eventbus.ExploreEvent
import com.masliaiev.filmspace.helpers.eventbus.HomeEvent
import com.masliaiev.filmspace.helpers.eventbus.MovieListEvent
import com.masliaiev.filmspace.helpers.eventbus.SearchEvent
import org.greenrobot.eventbus.EventBus

class MainFragment : Fragment() {

    private lateinit var navController: NavController


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

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigation) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        binding.bottomNavigation.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.home_navigation -> EventBus.getDefault().post(HomeEvent())
                R.id.explore_navigation -> {
                    EventBus.getDefault().apply {
                        post(ExploreEvent())
                        post(MovieListEvent())
                        post(SearchEvent())
                    }
                }
                R.id.library_navigation -> EventBus.getDefault().post(MovieListEvent())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}