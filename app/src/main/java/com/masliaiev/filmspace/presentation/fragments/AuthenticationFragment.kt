package com.masliaiev.filmspace.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentAuthenticationBinding
import com.masliaiev.filmspace.databinding.FragmentMainBinding

class AuthenticationFragment : Fragment() {


    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthenticationBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}