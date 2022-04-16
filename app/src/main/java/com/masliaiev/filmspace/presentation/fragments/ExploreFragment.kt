package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentExploreBinding
import com.masliaiev.filmspace.helpers.MovieListLaunchParams
import com.masliaiev.filmspace.presentation.adapters.GenreAdapter
import com.masliaiev.filmspace.presentation.view_models.ExploreFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import javax.inject.Inject

class ExploreFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ExploreFragmentViewModel

    private val adapter by lazy {
        GenreAdapter()
    }

    private var _binding: FragmentExploreBinding? = null
    private val binding: FragmentExploreBinding
        get() = _binding ?: throw RuntimeException("FragmentExploreBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ExploreFragmentViewModel::class.java]

        binding.rvGenres.adapter = adapter
        binding.rvGenres.layoutManager =
            GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false)

        viewModel.genres.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.tvTopRated.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(null, MovieListLaunchParams.TOP_RATED)
            )
        }

        binding.tvPopular.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(null, MovieListLaunchParams.POPULAR)
            )
        }

        binding.tvNowPlaying.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(null, MovieListLaunchParams.NOW_PLAYING)
            )
        }

        binding.tvUpcoming.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(null, MovieListLaunchParams.UPCOMING)
            )
        }

        binding.tvTrendingToday.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(null, MovieListLaunchParams.TRENDING_TODAY)
            )
        }

        binding.tvNowPlaying.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(null, MovieListLaunchParams.TRENDING_WEEK)
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}