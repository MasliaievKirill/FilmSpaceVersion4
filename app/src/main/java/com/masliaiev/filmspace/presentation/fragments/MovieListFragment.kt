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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentMovieListBinding
import com.masliaiev.filmspace.helpers.MovieListLaunchParams
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.adapters.MoviePagerAdapter
import com.masliaiev.filmspace.presentation.adapters.OnMovieClickListener
import com.masliaiev.filmspace.presentation.view_models.MovieListFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.movieListToolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }

        binding.movieListToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel =
            ViewModelProvider(this, viewModelFactory)[MovieListFragmentViewModel::class.java]

        binding.rvMovieList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvMovieList.adapter = adapter

        binding.movieListToolbar.title = args.title

        when (args.launchParam) {
            MovieListLaunchParams.GENRE -> {
                args.genre?.let {
                    viewModel.getMoviesByGenre(args.genre.toString())
                    observeViewModel()
                }
            }
            MovieListLaunchParams.FAVOURITE -> {
                viewModel.getAllFavouriteMovies()
                observeViewModel()
            }
            MovieListLaunchParams.WATCHLIST -> {
                viewModel.getAllMoviesWatchlist()
                observeViewModel()
            }
            MovieListLaunchParams.RATED -> {
                viewModel.getAllRatedMovies()
                observeViewModel()
            }
            MovieListLaunchParams.NOW_PLAYING -> {
                viewModel.getAllNowPlayingMovies()
                observeViewModel()
            }
            MovieListLaunchParams.POPULAR -> {
                viewModel.getAllPopularMovies()
                observeViewModel()
            }
            MovieListLaunchParams.UPCOMING -> {
                viewModel.getAllUpcomingMovies()
                observeViewModel()
            }
            MovieListLaunchParams.TOP_RATED -> {
                viewModel.getAllTopRatedMovies()
                observeViewModel()
            }
            MovieListLaunchParams.TRENDING_TODAY -> {
                viewModel.getTrendingDayMovies()
                observeViewModel()
            }
            MovieListLaunchParams.TRENDING_WEEK -> {
                viewModel.getTrendingWeekMovies()
                observeViewModel()
            }

        }
        adapter.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                        movieId
                    )
                )
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.movies?.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }
}