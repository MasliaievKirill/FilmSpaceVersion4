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
import com.masliaiev.filmspace.AppConstants
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.databinding.FragmentAccountBinding
import com.masliaiev.filmspace.helpers.findTopNavController
import com.masliaiev.filmspace.presentation.view_models.AccountFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import com.squareup.picasso.Picasso
import javax.inject.Inject

class AccountFragment : Fragment() {

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: AccountFragmentViewModel

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding ?: throw RuntimeException("FragmentAccountBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateLayout()

        binding.accountToolbar.setNavigationOnClickListener {
            findTopNavController().popBackStack()
        }

        viewModel =
            ViewModelProvider(this, viewModelFactory)[AccountFragmentViewModel::class.java]

        when (viewModel.appMode) {

            AppConstants.SIGNED_IN_MODE -> {
                showAccountModeLayout()
                viewModel.account.observe(viewLifecycleOwner) {
                    binding.tvName.text = it.name
                    binding.tvLogin.text = it.username
                    if (it.avatarPath != EMPTY_AVATAR_PATH) {
                        Picasso.get().load(it.avatarPath).into(binding.ivAvatar)
                    }
                }
                viewModel.deleteSessionSuccess.observe(viewLifecycleOwner) {
                    with(viewModel) {
                        setAppMode(AppConstants.UNKNOWN_MODE)
                        setSessionId(AppConstants.EMPTY_SESSION_ID)
                    }
                    findTopNavController().navigate(
                        AccountFragmentDirections.actionAccountFragmentToAuthenticationFragment()
                    )
                }
                viewModel.error.observe(viewLifecycleOwner) {
                    hideProgressbar()
                    showAccountModeLayout()
                    DialogWarningFragment.showCommonErrorDialogFragment(parentFragmentManager)
                }
                viewModel.apiError.observe(viewLifecycleOwner) {
                    with(viewModel) {
                        setAppMode(AppConstants.UNKNOWN_MODE)
                        setSessionId(AppConstants.EMPTY_SESSION_ID)
                    }
                    findTopNavController().navigate(
                        AccountFragmentDirections.actionAccountFragmentToAuthenticationFragment()
                    )
                }
                binding.buttonLogout.setOnClickListener {
                    hideAccountModeLayout()
                    showProgressbar()
                    viewModel.deleteSession()
                }
            }
            AppConstants.GUEST_MODE -> {
                binding.tvGuestMode.visibility = View.VISIBLE
                binding.tvGuestModeInfo.visibility = View.VISIBLE
                binding.buttonToAuthentication.visibility = View.VISIBLE
                binding.buttonToAuthentication.setOnClickListener {
                    with(viewModel) {
                        setAppMode(AppConstants.UNKNOWN_MODE)
                    }
                    findTopNavController().navigate(
                        AccountFragmentDirections.actionAccountFragmentToAuthenticationFragment()
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.accountToolbar) { v, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = insets.top)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun showAccountModeLayout() {
        binding.svAccountMode.visibility = View.VISIBLE
    }

    private fun hideAccountModeLayout() {
        binding.svAccountMode.visibility = View.INVISIBLE
    }

    private fun showProgressbar() {
        binding.pbAccount.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        binding.pbAccount.visibility = View.INVISIBLE
    }

    companion object {
        private const val EMPTY_AVATAR_PATH =
            "https://secure.gravatar.com/avatar/5a37619a4ab74125fde7619add58bdb9"
    }
}