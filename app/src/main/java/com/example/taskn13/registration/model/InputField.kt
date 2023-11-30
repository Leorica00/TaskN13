package com.example.taskn13.registration.model

import com.google.gson.annotations.SerializedName

data class InputField(
    @SerializedName("field_id")
    val fieldId: Int,
    val hint: String,
    @SerializedName("field_type")
    val fieldType: String,
    val keyboard: String,
    val required: Boolean,
    @SerializedName("is_active")
    val isActive: Boolean,
    val icon: Boolean,
    var inputValue: String = ""
)

enum class FieldType(val type: String) {
    INPUT("input"),
    CHOOSER("chooser")
}

enum class InputTextType(val text: String) {
    Text("text"),
    NUMBER("number")
}

enum class ChooserType(val hint: String) {
    DATE("Birthday"),
    SPINNER("Gender")
}

enum class GenderOptions(val gender: String) {
    MALE("Male"),
    FEMALE("Female")
}

