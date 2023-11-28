package com.example.taskn13.register.model

import com.google.gson.annotations.SerializedName

data class InputField(
    @SerializedName("field_id")
    val fieldId : Int,
    val hint: String,
    @SerializedName("field_type")
    val fieldType: String,
    val keyboard: String,
    val required: Boolean,
    @SerializedName("is_active")
    val isActive: Boolean,
    val icon: Boolean
)

