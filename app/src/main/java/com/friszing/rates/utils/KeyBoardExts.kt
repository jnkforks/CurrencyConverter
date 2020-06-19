package com.friszing.rates.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.closeKeyBoard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(
            windowToken,
            0
        )
}