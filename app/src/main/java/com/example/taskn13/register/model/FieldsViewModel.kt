package com.example.taskn13.register.model

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.taskn13.JsonFileReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FieldsViewModel: ViewModel() {
    fun parseJson(context: Context): List<List<InputField>> {
        val gson = Gson()
        val jsonString = JsonFileReader().loadJSONFromAsset(context, "fields.json")
        val inputs = object : TypeToken<List<List<InputField>>>() {}.type
        return gson.fromJson(jsonString, inputs)
    }
}