package com.masliaiev.filmspace.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentWebAuthenticationBinding
import com.masliaiev.filmspace.domain.entity.AuthParams
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import com.masliaiev.filmspace.presentation.view_models.WebAuthFragmentViewModel
import javax.inject.Inject

class WebAuthFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: WebAuthFragmentViewModel

    private val args by navArgs<WebAuthFragmentArgs>()

    private var _binding: FragmentWebAuthenticationBinding? = null
    private val binding: FragmentWebAuthenticationBinding
        get() = _binding ?: throw RuntimeException("FragmentWebAuthenticationBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[WebAuthFragmentViewModel::class.java]
        binding.wvAuth.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.pbWebAuth.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.pbWebAuth.visibility = View.INVISIBLE
                url?.let {
                    if (url.contains(RESPONSE_ALLOW)) {
                        binding.pbWebAuth.visibility = View.VISIBLE
                        viewModel.createSession(args.requestToken)
                        viewModel.createSessionResponse.observe(viewLifecycleOwner) {
                            if (it.success) {
                                binding.pbWebAuth.visibility = View.INVISIBLE
                                with(viewModel) {
                                    setAppMode(AppConstants.SIGNED_IN_MODE)
                                    setSessionId(it.sessionId)
                                }
                                findNavController().navigate(
                                    WebAuthFragmentDirections.actionWebAuthenticationFragmentToAuthInfoFragment(
                                        AuthParams.ALLOW
                                    )
                                )
                            } else {
                                findNavController().navigate(
                                    WebAuthFragmentDirections.actionWebAuthenticationFragmentToAuthInfoFragment(
                                        AuthParams.DENY
                                    )
                                )
                            }
                        }
                    } else if (url.contains(RESPONSE_DENY)) {
                        with(viewModel) {
                            setAppMode(AppConstants.GUEST_MODE)
                            setSessionId(AppConstants.EMPTY_SESSION_ID)
                        }
                        findNavController().navigate(
                            WebAuthFragmentDirections.actionWebAuthenticationFragmentToAuthInfoFragment(
                                AuthParams.DENY
                            )
                        )
                    }
                }
            }

        }
        binding.wvAuth.settings.javaScriptEnabled = true
        binding.wvAuth.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.wvAuth.loadUrl(REQUEST_URL + args.requestToken)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val RESPONSE_ALLOW = "allow"
        private const val RESPONSE_DENY = "deny"

        private const val REQUEST_URL = "https://www.themoviedb.org/authenticate/"
    }
}