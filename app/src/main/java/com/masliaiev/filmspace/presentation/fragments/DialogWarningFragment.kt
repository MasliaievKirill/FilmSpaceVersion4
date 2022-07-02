package com.masliaiev.filmspace.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.masliaiev.filmspace.R

class DialogWarningFragment : DialogFragment() {

    private lateinit var dialogMode: String
    private lateinit var dialogHeader: String
    private lateinit var dialogMessage: String


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        parseParams()
        val dialog = AlertDialog.Builder(requireActivity())
            .setTitle(dialogHeader)
            .setMessage(dialogMessage)
            .setPositiveButton("OK") { _, which ->
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(RESPONSE_KEY to true)
                )
                dismiss()

            }
            .create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP){
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(RESPONSE_KEY to true)
                )
                dismiss()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        return dialog
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

        private const val TAG = "warning_dialog"

        private const val DIALOG_MODE = "dialog_mode"

        private const val API_ERROR = "api_error"
        private const val COMMON_ERROR = "common_error"

        private const val REQUEST_KEY = "request_key_dialog_warning"
        private const val RESPONSE_KEY = "back"

        fun showApiErrorDialogFragment(fragmentManager: FragmentManager) {
            DialogWarningFragment().apply {

                arguments = Bundle().apply {
                    putString(DIALOG_MODE, API_ERROR)
                }
            }.show(fragmentManager, TAG)
        }

        fun showCommonErrorDialogFragment(fragmentManager: FragmentManager) {
            DialogWarningFragment().apply {
                arguments = Bundle().apply {
                    putString(DIALOG_MODE, COMMON_ERROR)
                }
            }.show(fragmentManager, TAG)
        }

        fun setOnOkListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (Boolean) -> Unit
        ) {
            fragmentManager.setFragmentResultListener(
                REQUEST_KEY,
                lifecycleOwner
            ) { _, result ->
                listener.invoke(result.getBoolean(RESPONSE_KEY))
            }
        }

    }


}