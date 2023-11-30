package com.example.taskn13.registration

import com.example.taskn13.registration.model.InputField

interface InnerAdapterCallback {
    fun onInnerDataChanged(updatedInputField: InputField)
}