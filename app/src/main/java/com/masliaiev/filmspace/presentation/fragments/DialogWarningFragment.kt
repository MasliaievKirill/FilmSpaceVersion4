package com.masliaiev.filmspace.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.masliaiev.filmspace.R
import com.masliaiev.filmspace.databinding.FragmentDialogWarningBinding

class DialogWarningFragment : DialogFragment() {

    private lateinit var dialogMode: String
    private lateinit var dialogHeader: String
    private lateinit var dialogMessage: String

    private var _binding: FragmentDialogWarningBinding? = null
    private val binding: FragmentDialogWarningBinding
        get() = _binding ?: throw RuntimeException("FragmentDialogWarningBinding is null")


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        parseParams()
        return AlertDialog.Builder(requireActivity())
            .setTitle(dialogHeader).setMessage(dialogMessage).setPositiveButton("OK", null).create()
    }


    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(DIALOG_MODE)) {
            throw RuntimeException("Dialog warning fragment param is absent")
        }
        val mode = args.getString(DIALOG_MODE)
        if (mode != API_ERROR && mode != COMMON_ERROR) {
            throw RuntimeException("Dialog warning fragment: unknown mode: $mode")
        }
        dialogMode = mode
        if (dialogMode == COMMON_ERROR) {
            dialogHeader = getString(R.string.dialog_common_error_header)
            dialogMessage = getString(R.string.dialog_common_error_description)
        } else {
            dialogHeader = getString(R.string.dialog_api_error_header)
            dialogMessage = getString(R.string.dialog_api_error_description)
        }

    }

    companion object {
        private const val DIALOG_MODE = "dialog_mode"

        private const val API_ERROR = "api_error"
        private const val COMMON_ERROR = "common_error"

        fun newInstanceApiError(): DialogWarningFragment {
            return DialogWarningFragment().apply {
                arguments = Bundle().apply {
                    putString(DIALOG_MODE, API_ERROR)
                }
            }
        }


        fun newInstanceCommonError(): DialogWarningFragment {
            return DialogWarningFragment().apply {
                arguments = Bundle().apply {
                    putString(DIALOG_MODE, COMMON_ERROR)
                }
            }
        }


    }


}