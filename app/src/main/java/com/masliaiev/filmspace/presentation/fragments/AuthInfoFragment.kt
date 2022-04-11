package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentAuthInfoBinding
import com.masliaiev.filmspace.domain.entity.AuthParams

class AuthInfoFragment : Fragment() {


    private var _binding: FragmentAuthInfoBinding? = null
    private val binding: FragmentAuthInfoBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthInfoBinding is null")


    private val args by navArgs<AuthInfoFragmentArgs>()

    override fun onAttach(context: Context) {

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (args.mode) {
            AuthParams.ALLOW -> {
                binding.tvModeInfo.text = getString(R.string.auth_info_approved)
            }
            AuthParams.DENY -> {
                binding.tvModeInfo.text = getString(R.string.auth_info_not_confirmed)
            }
            AuthParams.GUEST -> {
                binding.tvModeInfo.text = getString(R.string.auth_info_guest)
            }
        }

        binding.buttonContinue.setOnClickListener {
            findNavController().popBackStack()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}