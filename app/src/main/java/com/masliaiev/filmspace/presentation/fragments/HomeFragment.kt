package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentHomeBinding
import com.masliaiev.filmspace.presentation.adapters.HomeMovieAdapter
import com.masliaiev.filmspace.presentation.view_models.HomeFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import javax.inject.Inject

class HomeFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: HomeFragmentViewModel

    private val adapter by lazy {
        HomeMovieAdapter()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw RuntimeException("FragmentHomeBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("HomeFragment",  "onViewCreated")

        viewModel =
            ViewModelProvider(this, viewModelFactory)[HomeFragmentViewModel::class.java]

//        viewModel.popularMovies.observe(viewLifecycleOwner){
//            Log.d("HomeFragment",  "Popular: ${it.toString()}")
//        }

        binding.rvNowPlaying.adapter = adapter
        binding.rvNowPlaying.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.nowPlayingMovies.observe(viewLifecycleOwner) {
            Log.d("HomeFragment",  "nowPlaying: ${it.toString()}")
            adapter.submitList(it)
        }
//        viewModel.error.observe(viewLifecycleOwner) {
//            Log.d("HomeFragment",  "error: ${it.toString()}")
//        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.nowPlayingMovies.removeObservers(viewLifecycleOwner)
        Log.d("HomeFragment",  "onDestroyView")
    }
}