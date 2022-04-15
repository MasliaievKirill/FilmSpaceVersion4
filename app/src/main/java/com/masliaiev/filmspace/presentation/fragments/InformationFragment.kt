package com.masliaiev.filmspace.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.masliaiev.filmspace.databinding.FragmentInformationBinding
import com.masliaiev.filmspace.helpers.findTopNavController

class InformationFragment : Fragment() {

    private var _binding: FragmentInformationBinding? = null
    private val binding: FragmentInformationBinding
        get() = _binding ?: throw RuntimeException("FragmentInformationBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.informationToolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }

        binding.informationToolbar.setNavigationOnClickListener {
            findTopNavController().popBackStack()
        }

        binding.tvPrivacyPolicy.setOnClickListener {
            launchChromeTab(PRIVACY_POLICY_URL)
        }

        binding.tvTmdbApi.setOnClickListener {
            launchChromeTab(TMDB_URL)
        }

        binding.tvGraphicResources.setOnClickListener {
            launchChromeTab(REMIX_ICON_URL)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchChromeTab(url: String) {
        val tabIntent = CustomTabsIntent.Builder().setStartAnimations(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_in,
            com.google.android.material.R.anim.abc_fade_out
        ).setExitAnimations(
            requireContext(),
            com.google.android.material.R.anim.abc_fade_in,
            com.google.android.material.R.anim.abc_fade_out
        ).build()
        tabIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    companion object {

        private const val PRIVACY_POLICY_URL =
            "https://sites.google.com/view/privacypolicyoffilmspaceapp"
        private const val TMDB_URL = "https://www.themoviedb.org/"
        private const val REMIX_ICON_URL = "https://remixicon.com/"
    }
}