package com.masliaiev.filmspace.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.masliaiev.filmspace.R

class DialogRateFragment : DialogFragment() {


    private val ratingValues =
        arrayOf("1.0", "2.0", "3.0", "4.0", "5.0", "6.0", "7.0", "8.0", "9.0", "10.0")
    private var currentRatingValuePosition = -1


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        parseParams()
        return AlertDialog.Builder(requireActivity())
            .setTitle("Select rating")
            .setSingleChoiceItems(ratingValues, currentRatingValuePosition) { _, which ->
                val choseRatingValue = ratingValues[which].toDouble()
                parentFragmentManager.setFragmentResult(
                    REQUEST_RATE_KEY,
                    bundleOf(RESPONSE_RATE_KEY to choseRatingValue)
                )
                dismiss()
            }
            .setNeutralButton("Delete rating") { dialog, which ->
                parentFragmentManager.setFragmentResult(
                    REQUEST_DELETE_RATING_KEY, bundleOf(
                        RESPONSE_DELETE_RATING_KEY to true
                    )
                )
                dismiss()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }


    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(RATE_VALUE)) {
            throw RuntimeException("Dialog rate fragment param is absent")
        }
        val value = args.getDouble(RATE_VALUE)
        if (value != 0.0) {
            for (position in ratingValues.indices) {
                if (ratingValues[position].toDouble() == value) {
                    currentRatingValuePosition = position
                }
            }
        }
    }

    companion object {

        private const val RATE_VALUE = "rate_value"
        private const val TAG = "dialog_rate_fragment"
        private const val REQUEST_RATE_KEY = "request_key_dialog_rate"
        private const val REQUEST_DELETE_RATING_KEY = "request_key_dialog_delete_rating"
        private const val RESPONSE_RATE_KEY = "rate_value"
        private const val RESPONSE_DELETE_RATING_KEY = "delete_rating"

        fun show(fragmentManager: FragmentManager, rateValue: Double) {
            DialogRateFragment().apply {
                arguments = Bundle().apply {
                    putDouble(RATE_VALUE, rateValue)
                }
            }.show(fragmentManager, TAG)
        }

        fun setOnItemListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (Double) -> Unit
        ) {
            fragmentManager.setFragmentResultListener(
                REQUEST_RATE_KEY,
                lifecycleOwner
            ) { _, result ->
                listener.invoke(result.getDouble(RESPONSE_RATE_KEY))
            }
        }

        fun setOnDeleteRatingListener(
            fragmentManager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (Boolean) -> Unit
        ) {
            fragmentManager.setFragmentResultListener(
                REQUEST_DELETE_RATING_KEY,
                lifecycleOwner
            ) { _, result ->
                listener.invoke(result.getBoolean(RESPONSE_DELETE_RATING_KEY))
            }
        }

    }

}