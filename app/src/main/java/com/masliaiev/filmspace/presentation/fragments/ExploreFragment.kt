package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentExploreBinding
import com.masliaiev.filmspace.helpers.MovieListLaunchParams
import com.masliaiev.filmspace.presentation.adapters.GenreAdapter
import com.masliaiev.filmspace.presentation.adapters.OnGenreClickListener
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.tvExplore) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }



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
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.TOP_RATED,
                    binding.tvTopRated.text.toString()
                )
            )
        }

        binding.tvPopular.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.POPULAR,
                    binding.tvPopular.text.toString()
                )
            )
        }

        binding.tvNowPlaying.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.NOW_PLAYING,
                    binding.tvNowPlaying.text.toString()
                )
            )
        }

        binding.tvUpcoming.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.UPCOMING,
                    binding.tvUpcoming.text.toString()
                )
            )
        }

        binding.tvTrendingToday.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.TRENDING_TODAY,
                    binding.tvTrendingToday.text.toString()
                )
            )
        }

        binding.tvTrendingWeek.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.TRENDING_WEEK,
                    binding.tvTrendingWeek.text.toString()
                )
            )
        }

        adapter.onGenreClickListener = object : OnGenreClickListener{
            override fun onGenreClick(genreId: String, genreName: String) {
                findNavController().navigate(
                    ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                        genreId,
                        MovieListLaunchParams.GENRE,
                        genreName
                    )
                )
            }
        }

        binding.ivSearch.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToSearchFragment()
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}