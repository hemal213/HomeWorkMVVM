package com.imaginato.homeworkmvvm.exts

import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

/***
 * Clear error from text input layout
 */
fun TextInputLayout.clearError() {
    error = null
    isErrorEnabled = false
}

/***
 * Remove Error
 */
fun TextInputLayout.removeError(){
    editText?.addTextChangedListener(object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if(!s.isNullOrEmpty()){
                error = null
                isErrorEnabled = false
            }

        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}