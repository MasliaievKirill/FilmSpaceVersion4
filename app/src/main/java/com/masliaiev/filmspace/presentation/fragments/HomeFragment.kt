package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentHomeBinding
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.adapters.HomeMovieAdapter
import com.masliaiev.filmspace.presentation.adapters.OnMovieClickListener
import com.masliaiev.filmspace.presentation.view_models.HomeFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: HomeFragmentViewModel

    private val adapterNowPlaying by lazy {
        HomeMovieAdapter()
    }

    private val adapterTrendingWeek by lazy {
        HomeMovieAdapter()
    }
    private val adapterTrendingDay by lazy {
        HomeMovieAdapter()
    }
    private val adapterUpcoming by lazy {
        HomeMovieAdapter()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeToolbar.inflateMenu(R.menu.toolbar_menu)

        binding.homeToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_account -> {
                    findTopNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToAccountFragment()
                    )
                }
                R.id.action_information -> {
                    findTopNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToInformationFragment()
                    )
                }
            }
            true
        }

        Log.d("HomeFragment", "onViewCreated")

        viewModel =
            ViewModelProvider(this, viewModelFactory)[HomeFragmentViewModel::class.java]

        adapterNowPlaying.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        adapterTrendingWeek.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        adapterTrendingDay.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        adapterUpcoming.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        binding.rvNowPlaying.adapter = adapterNowPlaying
        binding.rvNowPlaying.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvTrendingWeek.adapter = adapterTrendingWeek
        binding.rvTrendingWeek.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvTrendingToday.adapter = adapterTrendingDay
        binding.rvTrendingToday.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvUpcoming.adapter = adapterUpcoming
        binding.rvUpcoming.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) {
            Log.d("HomeFragment", "nowPlaying: ${it.toString()}")
            adapterNowPlaying.submitList(it)
        }

        viewModel.trendingWeek.observe(viewLifecycleOwner) {
            adapterTrendingWeek.submitList(it)
        }

        viewModel.trendingDay.observe(viewLifecycleOwner) {
            adapterTrendingDay.submitList(it)
        }

        viewModel.upcomingMovies.observe(viewLifecycleOwner) {
            adapterUpcoming.submitList(it)
        }


//        viewModel.error.observe(viewLifecycleOwner) {
//            Log.d("HomeFragment",  "error: ${it.toString()}")
//        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.homeToolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.nowPlayingMovies.removeObservers(viewLifecycleOwner)
        Log.d("HomeFragment", "onDestroyView")
    }
}