package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentAuthenticationBinding
import com.masliaiev.filmspace.domain.entity.AuthParams
import com.masliaiev.filmspace.presentation.view_models.AuthenticationFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import javax.inject.Inject

class AuthFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: AuthenticationFragmentViewModel

    private var _binding: FragmentAuthenticationBinding? = null
    private val binding: FragmentAuthenticationBinding
        get() = _binding ?: throw RuntimeException("FragmentAuthenticationBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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

        viewModel =
            ViewModelProvider(this, viewModelFactory)[AuthenticationFragmentViewModel::class.java]

        viewModel.error.observe(viewLifecycleOwner) {
            changeVisibilityIfError()
            DialogWarningFragment.showCommonErrorDialogFragment(parentFragmentManager)
        }
        viewModel.apiError.observe(viewLifecycleOwner) {
            changeVisibilityIfError()
            DialogWarningFragment.showApiErrorDialogFragment(parentFragmentManager)
        }
        viewModel.requestTokenResponse.observe(viewLifecycleOwner) {
            if (it.success) {
                findNavController().navigate(
                    AuthFragmentDirections.actionAuthenticationFragmentToWebAuthenticationFragment(
                        it.requestToken
                    )
                )

            } else {
                changeVisibilityIfError()
                DialogWarningFragment.showApiErrorDialogFragment(parentFragmentManager)
            }
        }

        binding.buttonSignIn.setOnClickListener {
            changeVisibility()
            viewModel.createRequestToken()

        }
        binding.buttonGuest.setOnClickListener {
            with(viewModel) {
                setAppMode(AppConstants.GUEST_MODE)
                setSessionId(AppConstants.EMPTY_SESSION_ID)
            }
            findNavController().navigate(
                AuthFragmentDirections.actionAuthenticationFragmentToAuthInfoFragment(AuthParams.GUEST)
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun changeVisibility() {
        with(binding) {
            tvSignInInfo.visibility = View.INVISIBLE
            tvGuestInfo.visibility = View.INVISIBLE
            buttonSignIn.visibility = View.INVISIBLE
            buttonGuest.visibility = View.INVISIBLE
            pbAuth.visibility = View.VISIBLE
        }
    }

    private fun changeVisibilityIfError() {
        with(binding) {
            tvSignInInfo.visibility = View.VISIBLE
            tvGuestInfo.visibility = View.VISIBLE
            buttonSignIn.visibility = View.VISIBLE
            buttonGuest.visibility = View.VISIBLE
            pbAuth.visibility = View.INVISIBLE
        }
    }


}