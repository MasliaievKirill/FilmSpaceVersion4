package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.masliaiev.filmspace.helpers.*
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


        viewModel.loadData(args.movieId)

        observeViewModel()

        binding.ivWatchlist.setOnClickListener {
            viewModel.addRemoveToWatchlist(args.movieId)
            binding.ivWatchlist.isEnabled = false
        }

        binding.ivFavourite.setOnClickListener {
            viewModel.markAsFavourite(args.movieId)
            binding.ivFavourite.isEnabled = false
        }

        binding.ivRateStar.setOnClickListener {
            DialogRateFragment.show(parentFragmentManager, viewModel.rateMarker)
        }

        binding.ivShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/movie/${args.movieId}")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
        setDialogRateFragmentListener()
        setDialogWarningListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun disableActionButtons() {
        binding.ivRateStar.isEnabled = false
        binding.ivFavourite.isEnabled = false
        binding.ivWatchlist.isEnabled = false
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

        viewModel.accountMode.observe(viewLifecycleOwner) {
            showActionButtons()
        }

        viewModel.detailedMovie.observe(viewLifecycleOwner) {

            Picasso.get().load(it.backdropPath).placeholder(R.drawable.backdrop_placeholder)
                .into(binding.ivBackdrop)
            Picasso.get().load(it.posterPath).placeholder(R.drawable.poster_placeholder)
                .into(binding.ivPosterDetailed)

            with(binding) {
                detailMovieToolbar.title = it.title
                tvTitle.text = it.title
                tvOriginalTitle.text = it.originalTitle
                tvStatus.text = it.status
                if (it.overview == EMPTY_STRING) {
                    tvOverview.text = getString(R.string.no_data)
                } else {
                    tvOverview.text = it.overview
                }
                if (it.releaseDate == EMPTY_STRING) {
                    tvReleaseDate.text = getString(R.string.no_data)
                } else {
                    tvReleaseDate.text = it.releaseDate
                }
                if (it.genres == EMPTY_STRING) {
                    tvGenresDetail.text = getString(R.string.no_data)
                } else {
                    tvGenresDetail.text = it.genres
                }
                tvRating.text = it.voteAverage
                if (it.runtime == EMPTY_RUNTIME) {
                    tvRuntime.text = getString(R.string.no_data)
                } else {
                    tvRuntime.text = it.runtime
                }
            }
            hideProgressBar()
            binding.detailedMovieMainLayout.visibility = View.VISIBLE
        }
        viewModel.recommendations.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapterRecommendations.submitList(it)
            } else {
                with(binding) {
                    tvRecommendations.visibility = View.INVISIBLE
                    rvRecommendations.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.similarMovies.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapterSimilarMovies.submitList(it)
            } else {
                with(binding) {
                    tvSimilarMovies.visibility = View.INVISIBLE
                    rvSimilarMovies.visibility = View.INVISIBLE
                }
            }
        }
        viewModel.video.observe(viewLifecycleOwner) { video ->
            binding.ivPlayTrailer.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_play_circle_active
            )
            with(binding.tvPlayTrailerDescription) {
                text = getString(R.string.play_trailer_on_youtube)
                setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
            binding.ivPlayTrailer.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video.key)))
            }
        }
        viewModel.apiError.observe(viewLifecycleOwner) {
            hideProgressBar()
            DialogWarningFragment.showApiErrorDialogFragment(parentFragmentManager)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            hideProgressBar()
            DialogWarningFragment.showCommonErrorDialogFragment(parentFragmentManager)
        }
        viewModel.watchlist.observe(viewLifecycleOwner) {
            binding.pbWatchlist.visibility = View.INVISIBLE
            binding.ivWatchlist.isEnabled = true
            when (it) {
                is Result -> {
                    if (it.inList) {
                        binding.tvWatchlistDescription.text =
                            getString(R.string.in_watchlist_description)
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
                }
                is Error -> {
                    showErrorToast()
                    viewModel.setSavedWatchlistState()
                }
                is ApiError -> {
                    showApiErrorToast()
                    viewModel.setSavedWatchlistState()
                }
                is Progress -> {
                    binding.pbWatchlist.visibility = View.VISIBLE
                }
            }
        }
        viewModel.favourite.observe(viewLifecycleOwner) {
            binding.pbFavourite.visibility = View.INVISIBLE
            binding.ivFavourite.isEnabled = true
            when (it) {
                is Result -> {
                    if (it.inList) {
                        binding.tvFavouriteDescription.text =
                            getString(R.string.in_favourite_description)
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
                }
                is Error -> {
                    showErrorToast()
                    viewModel.setSavedFavouriteState()
                }
                is ApiError -> {
                    showApiErrorToast()
                    viewModel.setSavedFavouriteState()
                }
                is Progress -> {
                    binding.pbFavourite.visibility = View.VISIBLE
                }
            }
        }
        viewModel.rateMovie.observe(viewLifecycleOwner) {
            binding.pbRate.visibility = View.INVISIBLE
            binding.ivRateStar.isEnabled = true
            when (it) {
                is Result -> {
                    if (it.inList) {
                        binding.ivRateStar.background =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_star_rate_active
                            )
                        binding.tvRateDescription.text =
                            String.format(getString(R.string.is_rated_action), it.description)
                        viewModel.rateMarker = it.description.toDouble()
                    } else {
                        binding.tvRateDescription.text = getString(R.string.rate_action)
                        binding.ivRateStar.background =
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_star_rate_not_active
                            )
                        viewModel.rateMarker = EMPTY_RATING
                    }
                }
                is Error -> {
                    showErrorToast()
                    viewModel.setSavedRateState()
                }
                is ApiError -> {
                    showApiErrorToast()
                    viewModel.setSavedRateState()
                }
                is Progress -> {
                    binding.pbRate.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showErrorToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.dialog_common_error_description),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showApiErrorToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.dialog_api_error_description),
            Toast.LENGTH_SHORT
        ).show()
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

    private fun setDialogWarningListener() {
        DialogWarningFragment.setOnOkListener(parentFragmentManager, viewLifecycleOwner) {
            findTopNavController().popBackStack()
        }
    }

    private fun showActionButtons() {
        with(binding) {
            ivRateStar.visibility = View.VISIBLE
            ivFavourite.visibility = View.VISIBLE
            ivWatchlist.visibility = View.VISIBLE
            tvRateDescription.visibility = View.VISIBLE
            tvFavouriteDescription.visibility = View.VISIBLE
            tvWatchlistDescription.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        binding.pbDetailMovie.visibility = View.INVISIBLE
    }

    companion object {
        private const val EMPTY_RATING = 0.0
        private const val EMPTY_RUNTIME = "0"
        private const val EMPTY_STRING = ""
    }
}