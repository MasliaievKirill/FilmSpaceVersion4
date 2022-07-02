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
import androidx.recyclerview.widget.GridLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentExploreBinding
import com.masliaiev.filmspace.helpers.MovieListLaunchParams
import com.masliaiev.filmspace.helpers.eventbus.ExploreEvent
import com.masliaiev.filmspace.helpers.eventbus.HomeEvent
import com.masliaiev.filmspace.presentation.adapters.GenreAdapter
import com.masliaiev.filmspace.presentation.adapters.OnGenreClickListener
import com.masliaiev.filmspace.presentation.view_models.ExploreFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
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

        updateLayout()
        hideMainView()
        showProgressbar()

        viewModel =
            ViewModelProvider(this, viewModelFactory)[ExploreFragmentViewModel::class.java]

        binding.rvGenres.adapter = adapter
        binding.rvGenres.layoutManager =
            GridLayoutManager(
                requireContext(),
                SPAN_COUNT,
                GridLayoutManager.HORIZONTAL,
                false
            )

        binding.clTopRated.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.TOP_RATED,
                    binding.tvTopRated.text.toString()
                )
            )
        }

        binding.clPopular.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.POPULAR,
                    binding.tvPopular.text.toString()
                )
            )
        }

        binding.clNowPlaying.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.NOW_PLAYING,
                    binding.tvNowPlaying.text.toString()
                )
            )
        }

        binding.clUpcoming.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.UPCOMING,
                    binding.tvUpcoming.text.toString()
                )
            )
        }

        binding.clTrendingToday.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.TRENDING_TODAY,
                    binding.tvTrendingToday.text.toString()
                )
            )
        }

        binding.clTrendingWeek.setOnClickListener {
            findNavController().navigate(
                ExploreFragmentDirections.actionExploreFragmentToMovieListFragmentExplore(
                    null,
                    MovieListLaunchParams.TRENDING_WEEK,
                    binding.tvTrendingWeek.text.toString()
                )
            )
        }

        adapter.onGenreClickListener = object : OnGenreClickListener {
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

        binding.buttonTryAgainExplore.setOnClickListener {
            viewModel.tryAgain()
            hideWarning()
            showProgressbar()
        }

        observeViewModel()

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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ExploreEvent){
        binding.nestedScrollExplore.smoothScrollTo(0,0)
    }

    private fun updateLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.tvExplore) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun observeViewModel() {
        viewModel.genres.observe(viewLifecycleOwner) {
            hideProgressbar()
            showMainView()
            adapter.submitList(it)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it) {
                hideMainView()
                hideProgressbar()
                showWarning()
            }

        }
        viewModel.apiError.observe(viewLifecycleOwner) {
            if (it) {
                hideMainView()
                hideProgressbar()
                showWarning()
            }
        }
    }

    private fun showProgressbar() {
        binding.pbExplore.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        binding.pbExplore.visibility = View.INVISIBLE
    }

    private fun showWarning() {
        binding.ivWarningExplore.visibility = View.VISIBLE
        binding.tvWarningExplore.visibility = View.VISIBLE
        binding.buttonTryAgainExplore.visibility = View.VISIBLE
    }

    private fun hideWarning() {
        binding.ivWarningExplore.visibility = View.INVISIBLE
        binding.tvWarningExplore.visibility = View.INVISIBLE
        binding.buttonTryAgainExplore.visibility = View.INVISIBLE
    }

    private fun showMainView() {
        binding.nestedScrollExplore.visibility = View.VISIBLE
    }

    private fun hideMainView() {
        binding.nestedScrollExplore.visibility = View.INVISIBLE
    }

    companion object {
        private const val SPAN_COUNT = 4
    }
}