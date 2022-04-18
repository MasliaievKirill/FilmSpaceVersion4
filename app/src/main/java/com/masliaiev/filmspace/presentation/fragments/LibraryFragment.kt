package com.masliaiev.filmspace.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.masliaiev.filmspace.databinding.FragmentLibraryBinding
import com.masliaiev.filmspace.helpers.MovieListLaunchParams

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding: FragmentLibraryBinding
        get() = _binding ?: throw RuntimeException("FragmentLibraryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.tvLibrary) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }

        binding.tvWatchlist.setOnClickListener {
            findNavController().navigate(
                LibraryFragmentDirections.actionLibraryFragmentToMovieListFragment(
                    null,
                    MovieListLaunchParams.WATCHLIST,
                    binding.tvWatchlist.text.toString()
                )
            )
        }

        binding.tvFavouriteMovies.setOnClickListener {
            findNavController().navigate(
                LibraryFragmentDirections.actionLibraryFragmentToMovieListFragment(
                    null,
                    MovieListLaunchParams.FAVOURITE,
                    binding.tvFavouriteMovies.text.toString()
                )
            )
        }

        binding.tvRatedMovies.setOnClickListener {
            findNavController().navigate(
                LibraryFragmentDirections.actionLibraryFragmentToMovieListFragment(
                    null,
                    MovieListLaunchParams.RATED,
                    binding.tvRatedMovies.text.toString()
                )
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}