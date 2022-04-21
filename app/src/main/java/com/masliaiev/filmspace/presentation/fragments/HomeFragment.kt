package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
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

        updateLayout()
        setupToolbar()
        hideMainView()
        showProgressbar()

        binding.rvNowPlaying.adapter = adapterNowPlaying
        binding.rvNowPlaying.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterNowPlaying.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        binding.rvTrendingWeek.adapter = adapterTrendingWeek
        binding.rvTrendingWeek.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterTrendingWeek.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        binding.rvTrendingToday.adapter = adapterTrendingDay
        binding.rvTrendingToday.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterTrendingDay.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        binding.rvUpcoming.adapter = adapterUpcoming
        binding.rvUpcoming.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapterUpcoming.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }

        viewModel =
            ViewModelProvider(this, viewModelFactory)[HomeFragmentViewModel::class.java]

        observeViewModel()

        binding.buttonTryAgain.setOnClickListener {
            viewModel.tryAgain()
            hideWarning()
            showProgressbar()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) {
            adapterNowPlaying.submitList(it)
            hideProgressbar()
            showMainView()
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

        viewModel.error.observe(viewLifecycleOwner) {
            hideProgressbar()
            showWarning()
        }

        viewModel.apiError.observe(viewLifecycleOwner) {
            hideProgressbar()
            showWarning()
        }
    }

    private fun updateLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.homeToolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.homeToolbar.setLogo(R.drawable.astronaut_logo)
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
    }

    private fun showProgressbar() {
        binding.pbHome.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        binding.pbHome.visibility = View.INVISIBLE
    }

    private fun showWarning() {
        hideMainView()
        binding.ivWarning.visibility = View.VISIBLE
        binding.tvWarningHome.visibility = View.VISIBLE
        binding.buttonTryAgain.visibility = View.VISIBLE
    }

    private fun hideWarning() {
        showMainView()
        binding.ivWarning.visibility = View.INVISIBLE
        binding.tvWarningHome.visibility = View.INVISIBLE
        binding.buttonTryAgain.visibility = View.INVISIBLE
    }

    private fun showMainView() {
        binding.homeNestedScrollView.visibility = View.VISIBLE
    }

    private fun hideMainView() {
        binding.homeNestedScrollView.visibility = View.INVISIBLE
    }
}