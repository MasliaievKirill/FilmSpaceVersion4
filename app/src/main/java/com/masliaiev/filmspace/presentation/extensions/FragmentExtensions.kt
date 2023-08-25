package com.masliaiev.filmspace.presentation.extensions


import android.view.View
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

inline fun Fragment.updateLayoutWithInsets(
    view: View,
    crossinline block: (insets: Insets) -> Unit
) {
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
        block.invoke(insets.getInsets(WindowInsetsCompat.Type.systemBars()))
        insets
    }
}