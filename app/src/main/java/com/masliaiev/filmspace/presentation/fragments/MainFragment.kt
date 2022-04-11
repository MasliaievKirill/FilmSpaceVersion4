package com.masliaiev.filmspace.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.masliaiev.filmspace.FilmSpaceApp
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentMainBinding
import com.masliaiev.filmspace.presentation.view_models.MainFragmentViewModel
import com.masliaiev.filmspace.presentation.view_models.ViewModelFactory
import javax.inject.Inject

class MainFragment : Fragment() {

    private lateinit var navController: NavController

    private val component by lazy {
        (requireActivity().application as FilmSpaceApp).component
    }

    private lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainFragmentViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding ?: throw RuntimeException("FragmentMainBinding is null")

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        Log.d("Main", "onViewCreated")

        val appMode = sharedPreferences.getString(KEY_APP_MODE, UNKNOWN_MODE)

        Log.d("Main", sharedPreferences.getString(KEY_APP_MODE, UNKNOWN_MODE).toString())

        val sessionId = sharedPreferences.getString(KEY_SESSION_ID, "null")

        Log.d("Main", sharedPreferences.getString(KEY_SESSION_ID, "null").toString())



        when (appMode) {
            ACCOUNT_MODE -> {
                sessionId?.let {
                    viewModel.loadAccount(it)
                }
                Log.d("Main", "appM")
                viewModel.accountIsActive.observe(viewLifecycleOwner){
                    Log.d("Main", it.toString())
                    Log.d("Main", sessionId.toString())
                    if (!it){
                        sharedPreferences.edit().
                        putString(KEY_APP_MODE, UNKNOWN_MODE).
                        putString(KEY_SESSION_ID, "null")
                            .apply()
                        findNavController().navigate(
                            MainFragmentDirections.actionMainFragmentToAuthenticationFragment()
                        )

                    }
                }
            }
            UNKNOWN_MODE -> {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToAuthenticationFragment()
                )
            }
            GUEST_MODE -> {
                Log.d("Main", GUEST_MODE)
            }
        }







        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

    }

    override fun onPause() {
        super.onPause()
        Log.d("Main", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Main", "OnStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("Main", "onDestroyView")
    }

    companion object {

        const val APP_PREFERENCES = "film_space_app"

        const val KEY_APP_MODE = "app_mode"
        const val KEY_SESSION_ID = "session_id"

        const val UNKNOWN_MODE = "unknown"
        const val ACCOUNT_MODE = "account"
        const val GUEST_MODE = "guest"
    }


}