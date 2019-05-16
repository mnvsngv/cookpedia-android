package com.mnvsngv.cookpedia.fragment.adapter.listener

import android.text.Editable
import android.text.TextWatcher


class TextChangedListener(private val run: (position: Int, value: String) -> Unit): TextWatcher {
    private var position = -1

    fun updatePosition(newPosition: Int) {
        position = newPosition
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        run(position, s.toString())
    }
}