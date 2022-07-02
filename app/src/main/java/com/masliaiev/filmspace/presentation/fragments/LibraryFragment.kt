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
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentLibraryBinding
import com.masliaiev.filmspace.helpers.MovieListLaunchParams
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.view_models.LibraryFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import javax.inject.Inject

class LibraryFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: LibraryFragmentViewModel

    private var _binding: FragmentLibraryBinding? = null
    private val binding: FragmentLibraryBinding
        get() = _binding ?: throw RuntimeException("FragmentLibraryBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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

        updateLayout()

        viewModel =
            ViewModelProvider(this, viewModelFactory)[LibraryFragmentViewModel::class.java]

        when (viewModel.appMode) {
            AppConstants.SIGNED_IN_MODE -> {

                binding.clWatchlist.setOnClickListener {
                    findNavController().navigate(
                        LibraryFragmentDirections.actionLibraryFragmentToMovieListFragment(
                            null,
                            MovieListLaunchParams.WATCHLIST,
                            binding.tvWatchlist.text.toString()
                        )
                    )
                }

                binding.clFavouriteMovies.setOnClickListener {
                    findNavController().navigate(
                        LibraryFragmentDirections.actionLibraryFragmentToMovieListFragment(
                            null,
                            MovieListLaunchParams.FAVOURITE,
                            binding.tvFavouriteMovies.text.toString()
                        )
                    )
                }

                binding.clRatedMovies.setOnClickListener {
                    findNavController().navigate(
                        LibraryFragmentDirections.actionLibraryFragmentToMovieListFragment(
                            null,
                            MovieListLaunchParams.RATED,
                            binding.tvRatedMovies.text.toString()
                        )
                    )
                }
            }
            AppConstants.GUEST_MODE -> {
                showGuestModeLayout()
                binding.buttonToAccount.setOnClickListener {
                    findTopNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToAccountFragment()
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.tvLibrary) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun showGuestModeLayout() {
        binding.loggedInContent.visibility = View.INVISIBLE
        binding.tvLoggedOutMassage.visibility = View.VISIBLE
        binding.buttonToAccount.visibility = View.VISIBLE
    }
}