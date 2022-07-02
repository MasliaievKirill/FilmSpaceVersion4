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
import androidx.recyclerview.widget.LinearLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentSearchBinding
import com.masliaiev.filmspace.helpers.eventbus.SearchEvent
import com.masliaiev.filmspace.helpers.fastSmoothScrollToPosition
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.adapters.MoviePagerAdapter
import com.masliaiev.filmspace.presentation.adapters.OnMovieClickListener
import com.masliaiev.filmspace.presentation.view_models.SearchFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

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



        coroutineScope.launch {
            adapter.loadStateFlow.collectLatest {
                val refreshState = it.refresh
                with(binding) {
                    pbSearch.isVisible = refreshState is LoadState.Loading
                    if (refreshState is LoadState.Error) {
                        viewModel.setError()
                    }
                }
                if (refreshState is LoadState.NotLoading) {
                    binding.rvSearch.visibility = View.VISIBLE
                    binding.tvWelcomeSearch.visibility = View.INVISIBLE
                    binding.tvWelcomeSearchExtension.visibility = View.INVISIBLE
                    if (adapter.itemCount == 0) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.nothing_found),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvSearch.adapter = adapter

        observeViewModel()

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
                    viewModel.movies = null
                    binding.rvSearch.scrollToPosition(START_POSITION)
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

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        coroutineScope.coroutineContext.cancelChildren()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SearchEvent) {
        val layoutManager = (binding.rvSearch.layoutManager as LinearLayoutManager)
        if (layoutManager.findFirstVisibleItemPosition() != START_POSITION &&
            layoutManager.findFirstVisibleItemPosition() != EMPTY_RV_POSITION
        ) {
            binding.rvSearch.fastSmoothScrollToPosition(START_POSITION)
        } else {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.movies?.observe(viewLifecycleOwner) { movies ->
            adapter.submitData(viewLifecycleOwner.lifecycle, movies)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            if (it) {
                DialogWarningFragment.showCommonErrorDialogFragment(parentFragmentManager)
                binding.rvSearch.visibility = View.INVISIBLE
                binding.tvWelcomeSearch.visibility = View.VISIBLE
                binding.tvWelcomeSearchExtension.visibility = View.VISIBLE
                viewModel.clearError()
            }
        }
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
        private const val START_POSITION = 0
        private const val EMPTY_RV_POSITION = -1
    }

}