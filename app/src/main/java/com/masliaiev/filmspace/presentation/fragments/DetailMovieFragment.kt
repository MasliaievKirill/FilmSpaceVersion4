package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentDetailMovieBinding
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.adapters.HomeMovieAdapter
import com.masliaiev.filmspace.presentation.adapters.OnMovieClickListener
import com.masliaiev.filmspace.presentation.view_models.DetailMovieFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject

class DetailMovieFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: DetailMovieFragmentViewModel

    private val args by navArgs<DetailMovieFragmentArgs>()

    private val adapterRecommendations by lazy {
        HomeMovieAdapter()
    }

    private val adapterSimilarMovies by lazy {
        HomeMovieAdapter()
    }

    private var favouriteMarker = false
    private var watchlistMarker = false
    private var rateMarker = EMPTY_RATING

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding: FragmentDetailMovieBinding
        get() = _binding ?: throw RuntimeException("FragmentDetailMovieBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLayout()
        disableActionButtons()

        binding.detailMovieToolbar.setNavigationOnClickListener {
            findTopNavController().popBackStack()
        }

        binding.rvRecommendations.adapter = adapterRecommendations
        binding.rvRecommendations.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapterRecommendations.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    DetailMovieFragmentDirections.actionDetailMovieFragmentSelf(movieId)
                )
            }
        }

        binding.rvSimilarMovies.adapter = adapterSimilarMovies
        binding.rvSimilarMovies.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        adapterSimilarMovies.onMovieClickListener = object : OnMovieClickListener {
            override fun onMovieClick(movieId: Int) {
                findTopNavController().navigate(
                    DetailMovieFragmentDirections.actionDetailMovieFragmentSelf(movieId)
                )
            }
        }

        viewModel =
            ViewModelProvider(this, viewModelFactory)[DetailMovieFragmentViewModel::class.java]

        loadData()
        observeViewModel()

        binding.ivWatchlist.setOnClickListener {
            if (watchlistMarker) {
                viewModel.addRemoveToWatchlist(args.movieId, false)
            } else {
                viewModel.addRemoveToWatchlist(args.movieId, true)
            }
            binding.ivWatchlist.isEnabled = false
        }

        binding.ivFavourite.setOnClickListener {
            if (favouriteMarker) {
                viewModel.markAsFavourite(args.movieId, false)
            } else {
                viewModel.markAsFavourite(args.movieId, true)
            }
            binding.ivFavourite.isEnabled = false
        }

        binding.ivRateStar.setOnClickListener {
            DialogRateFragment.show(parentFragmentManager, rateMarker)
        }

        setDialogRateFragmentListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun enabledActionButtons() {
        binding.ivRateStar.isEnabled = true
        binding.ivFavourite.isEnabled = true
        binding.ivWatchlist.isEnabled = true
    }

    private fun disableActionButtons() {
        binding.ivRateStar.isEnabled = false
        binding.ivFavourite.isEnabled = false
        binding.ivWatchlist.isEnabled = false
    }

    private fun loadData() {
        viewModel.getAccountState(args.movieId)
        viewModel.getDetailedMovie(args.movieId)
        viewModel.getRecommendations(args.movieId)
        viewModel.getSimilarMovies(args.movieId)
    }

    private fun updateLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.detailedBar) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = binding.detailMovieToolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = insets.top
            binding.detailMovieToolbar.layoutParams = params
            binding.nestedScrollDetailed.updatePadding(bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun observeViewModel() {
        viewModel.detailedMovie.observe(viewLifecycleOwner) {

            Picasso.get().load(it.backdropPath).placeholder(R.drawable.backdrop_placeholder)
                .into(binding.ivBackdrop)
            Picasso.get().load(it.posterPath).placeholder(R.drawable.poster_placeholder)
                .into(binding.ivPosterDetailed)

            with(binding) {
                detailMovieToolbar.title = it.title
                tvOriginalTitle.text = it.originalTitle
                tvStatus.text = it.status
                tvOverview.text = it.overview
                tvReleaseDate.text = it.releaseDate
                tvVoteAverage.text = it.voteAverage
                tvGenresDetail.text = it.genres
            }
        }
        viewModel.recommendations.observe(viewLifecycleOwner) {
            adapterRecommendations.submitList(it)
        }
        viewModel.similarMovies.observe(viewLifecycleOwner) {
            adapterSimilarMovies.submitList(it)
        }
        viewModel.accountState.observe(viewLifecycleOwner) {
            if (it.rated is Boolean) {
                binding.tvRateDescription.text = getString(R.string.rate_action)
                binding.ivRateStar.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_rate_not_active)
                rateMarker = EMPTY_RATING
            } else {
                binding.ivRateStar.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_star_rate_active)
                val userRating = it.rated.toString()
                if (userRating[8].toString() == ".") {
                    rateMarker = userRating.subSequence(7, 10).toString().toDouble()
                } else {
                    rateMarker = userRating.subSequence(7, 11).toString().toDouble()
                }
                binding.tvRateDescription.text = String.format(getString(R.string.is_rated_action), rateMarker)

            }
            if (it.favorite) {
                binding.tvFavouriteDescription.text = getString(R.string.in_favourite_description)
                binding.ivFavourite.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_heart_favourite_active
                )
            } else {
                binding.tvFavouriteDescription.text =
                    getString(R.string.add_to_favourite_description)
                binding.ivFavourite.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_heart_favourite_not_active
                )
            }
            if (it.watchlist) {
                binding.tvWatchlistDescription.text = getString(R.string.in_watchlist_description)
                binding.ivWatchlist.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_watchlist_active
                )
            } else {
                binding.tvWatchlistDescription.text =
                    getString(R.string.add_to_watchlist_description)
                binding.ivWatchlist.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_watchlist_not_active
                )
            }
            favouriteMarker = it.favorite
            watchlistMarker = it.watchlist

            enabledActionButtons()

        }
        viewModel.addToWatchlist.observe(viewLifecycleOwner) {
            viewModel.getAccountState(args.movieId)
        }
        viewModel.markAsFavourite.observe(viewLifecycleOwner) {
            viewModel.getAccountState(args.movieId)
        }
        viewModel.rateMovie.observe(viewLifecycleOwner) {
            viewModel.getAccountState(args.movieId)
        }
        viewModel.deleteRating.observe(viewLifecycleOwner) {
            viewModel.getAccountState(args.movieId)
        }
        viewModel.apiError.observe(viewLifecycleOwner) {
            Log.d("Detail", "API error")
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Log.d("Detail", "error")
        }
    }

    private fun setDialogRateFragmentListener() {
        DialogRateFragment.apply {
            binding.ivRateStar.isEnabled = false
            setOnItemListener(parentFragmentManager, viewLifecycleOwner) {
                viewModel.rateMovie(it, args.movieId)
            }
            setOnDeleteRatingListener(parentFragmentManager, viewLifecycleOwner) {
                viewModel.deleteRating(args.movieId)
            }
        }
    }


    companion object {
        private const val EMPTY_RATING = 0.0
    }
}