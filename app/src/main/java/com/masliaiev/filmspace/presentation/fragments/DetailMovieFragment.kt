package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentDetailMovieBinding
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.adapters.HomeMovieAdapter
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.detailedBar) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            val params = binding.detailMovieToolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = insets.top
            binding.detailMovieToolbar.layoutParams = params

            WindowInsetsCompat.CONSUMED
        }

        binding.detailMovieToolbar.setNavigationOnClickListener {
            findTopNavController().popBackStack()
        }

        binding.rvRecommendations.adapter = adapterRecommendations
        binding.rvRecommendations.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel =
            ViewModelProvider(this, viewModelFactory)[DetailMovieFragmentViewModel::class.java]

        viewModel.getAccountState(args.movieId)
        viewModel.getDetailedMovie(args.movieId)
        viewModel.getRecommendations(args.movieId)
        viewModel.getSimilarMovies(args.movieId)

        viewModel.detailedMovie.observe(viewLifecycleOwner) {
            binding.detailMovieToolbar.title = it.title
            binding.tvOriginalTitle.text = it.originalTitle
            Picasso.get().load(it.backdropPath).into(binding.ivBackdrop)
            Picasso.get().load(it.posterPath).into(binding.ivPosterDetailed)
            binding.tvOverview.text = it.overview
            binding.tvReleaseDate.text = it.releaseDate
        }
        viewModel.recommendations.observe(viewLifecycleOwner) {
            adapterRecommendations.submitList(it)
        }

        viewModel.accountState.observe(viewLifecycleOwner){
            if (it.rated is Boolean){
                binding.tvRateDescription.text = "Rate!"
            } else {
                val userRating = it.rated.toString()
                if (userRating[8].toString() == "."){
                    binding.tvRateDescription.text = "You rated: ${userRating.subSequence(7, 10)}"
                } else{
                    binding.tvRateDescription.text = "You rated: ${userRating.subSequence(7, 11)}"
                }


            }
            if (it.favorite){
                binding.tvFavouriteDescription.text = "In favourite"
            } else{
                binding.tvFavouriteDescription.text = "Add to favourite"
            }
            if (it.watchlist){
                binding.tvWatchlistDescription.text = "In watchlist"
            } else{
                binding.tvWatchlistDescription.text = "Add to watchlist"
            }

        }

        viewModel.apiError.observe(viewLifecycleOwner){
            Log.d("Detail", "API error")
        }

        viewModel.error.observe(viewLifecycleOwner){
            Log.d("Detail", "error")
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}