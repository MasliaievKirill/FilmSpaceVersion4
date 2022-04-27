package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentSearchBinding
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.adapters.MoviePagerAdapter
import com.masliaiev.filmspace.presentation.adapters.OnMovieClickListener
import com.masliaiev.filmspace.presentation.view_models.SearchFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import javax.inject.Inject

class SearchFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: SearchFragmentViewModel

    private val adapter by lazy {
        MoviePagerAdapter()
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentSearchBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLayout()

        binding.searchToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel =
            ViewModelProvider(this, viewModelFactory)[SearchFragmentViewModel::class.java]

        binding.rvSearch.layoutManager = GridLayoutManager(requireContext(), SPAN_COUNT)

        adapter.addLoadStateListener {
            val refreshState = it.refresh
            with(binding) {
                pbSearch.isVisible = refreshState is LoadState.Loading
                rvSearch.isVisible = refreshState is LoadState.NotLoading
                if (refreshState is LoadState.Error) {
                    DialogWarningFragment.showCommonErrorDialogFragment(parentFragmentManager)
                    binding.tvWelcomeSearch.visibility = View.VISIBLE
                    binding.tvWelcomeSearchExtension.visibility = View.VISIBLE
                }
            }
            if (refreshState is LoadState.NotLoading) {
                binding.tvWelcomeSearch.visibility = View.INVISIBLE
                binding.tvWelcomeSearchExtension.visibility = View.INVISIBLE
                if (adapter.itemCount == 0){
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.nothing_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.rvSearch.adapter = adapter

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchMovies(it)
                    viewModel.movies?.hasObservers()?.let { hasObservers ->
                        if (hasObservers) {
                            viewModel.movies?.removeObservers(viewLifecycleOwner)
                        }
                    }
                    val inputMethodManager = requireContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(
                        binding.searchView.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    viewModel.movies?.observe(viewLifecycleOwner) { movies ->
                        adapter.submitData(viewLifecycleOwner.lifecycle, movies)
                    }
                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length == 0) {
                    binding.rvSearch.visibility = View.INVISIBLE
                    binding.tvWelcomeSearch.visibility = View.VISIBLE
                    binding.tvWelcomeSearchExtension.visibility = View.VISIBLE
                }
                return true
            }
        })

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

    private fun updateLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.searchToolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    companion object {
        private const val SPAN_COUNT = 2
    }


}