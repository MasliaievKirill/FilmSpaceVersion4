package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentMovieListBinding
import com.masliaiev.filmspace.helpers.MovieListLaunchParams
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.adapters.MoviePagerAdapter
import com.masliaiev.filmspace.presentation.adapters.OnMovieClickListener
import com.masliaiev.filmspace.presentation.view_models.MovieListFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class MovieListFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MovieListFragmentViewModel

    private val adapter by lazy {
        MoviePagerAdapter()
    }

    private var initialDataLoading = true

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private var _binding: FragmentMovieListBinding? = null
    private val binding: FragmentMovieListBinding
        get() = _binding ?: throw RuntimeException("FragmentMovieListBinding is null")

    private val args by navArgs<MovieListFragmentArgs>()

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLayout()
        setupToolbar()

        viewModel =
            ViewModelProvider(this, viewModelFactory)[MovieListFragmentViewModel::class.java]

        binding.rvMovieList.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)

        adapter.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }
        adapter.addLoadStateListener {
            val refreshState = it.refresh
            with(binding) {
                pbMovieList.isVisible = refreshState is LoadState.Loading
                rvMovieList.isVisible = refreshState !is LoadState.Error
                ivWarningMovieList.isVisible = refreshState is LoadState.Error
                tvWarningMovieList.isVisible = refreshState is LoadState.Error
                buttonTryAgainMovieList.isVisible = refreshState is LoadState.Error
            }
        }

        binding.rvMovieList.adapter = adapter

        when (args.launchParam) {
            MovieListLaunchParams.GENRE -> {
                args.genre?.let {
                    if (initialDataLoading) {
                        viewModel.getMoviesByGenre(args.genre.toString())
                        initialDataLoading = false
                    }
                    observeViewModel()
                }
            }
            MovieListLaunchParams.FAVOURITE -> {
                if (initialDataLoading) {
                    coroutineScope.launch {
                        viewModel.getAllFavouriteMovies()
                        observeViewModel()
                        initialDataLoading = false
                    }
                } else {
                    observeViewModel()
                }

            }
            MovieListLaunchParams.WATCHLIST -> {
                if (initialDataLoading) {
                    coroutineScope.launch {
                        viewModel.getAllMoviesWatchlist()
                        observeViewModel()
                        initialDataLoading = false
                    }
                } else {
                    observeViewModel()
                }

            }
            MovieListLaunchParams.RATED -> {
                if (initialDataLoading) {
                    coroutineScope.launch {
                        viewModel.getAllRatedMovies()
                        observeViewModel()
                        initialDataLoading = false
                    }
                } else {
                    observeViewModel()
                }

            }
            MovieListLaunchParams.NOW_PLAYING -> {
                if (initialDataLoading) {
                    viewModel.getAllNowPlayingMovies()
                    initialDataLoading = false
                }
                observeViewModel()
            }
            MovieListLaunchParams.POPULAR -> {
                if (initialDataLoading) {
                    viewModel.getAllPopularMovies()
                    initialDataLoading = false
                }

                observeViewModel()
            }
            MovieListLaunchParams.UPCOMING -> {
                if (initialDataLoading) {
                    viewModel.getAllUpcomingMovies()
                    initialDataLoading = false
                }
                observeViewModel()
            }
            MovieListLaunchParams.TOP_RATED -> {
                if (initialDataLoading) {
                    viewModel.getAllTopRatedMovies()
                    initialDataLoading = false
                }
                observeViewModel()
            }
            MovieListLaunchParams.TRENDING_TODAY -> {
                if (initialDataLoading) {
                    viewModel.getTrendingDayMovies()
                    initialDataLoading = false
                }

                observeViewModel()
            }
            MovieListLaunchParams.TRENDING_WEEK -> {
                if (initialDataLoading) {
                    viewModel.getTrendingWeekMovies()
                    initialDataLoading = false
                }
                observeViewModel()
            }
        }

        binding.buttonTryAgainMovieList.setOnClickListener {
            adapter.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun updateLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.movieListToolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        binding.movieListToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.movieListToolbar.title = args.title
    }

    private fun observeViewModel() {
        viewModel.movies?.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    companion object {
        private const val SPAN_COUNT = 2
    }
}