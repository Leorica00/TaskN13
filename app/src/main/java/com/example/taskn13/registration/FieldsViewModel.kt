package com.example.taskn13.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskn13.registration.model.InputField
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FieldsViewModel: ViewModel() {

    private val _fieldsData = MutableLiveData<List<List<InputField>>>()
    val fieldsData: LiveData<List<List<InputField>>>
        get() = _fieldsData

    init {
        val data = parseJson()
        data.forEach { list ->
            list.map { it.inputValue = "" }
        }
        _fieldsData.value = data
    }

    fun updateInputField(updatedInputField: InputField) {
        val currentData = _fieldsData.value.orEmpty().toMutableList()

        for (rowIndex in currentData.indices) {
            val columnIndex = currentData[rowIndex].indexOfFirst { it.fieldId == updatedInputField.fieldId }
            if (columnIndex != -1) {
                val updatedRow = currentData[rowIndex].toMutableList()
                updatedRow[columnIndex] = updatedInputField
                currentData[rowIndex] = updatedRow
                _fieldsData.value = currentData
                return
            }
        }
    }

    private fun parseJson(): List<List<InputField>> {
        val jsonData =
            "[\n" +
                    "  [\n" +
                    "    {\n" +
                    "      \"field_id\":1,\n" +
                    "      \"hint\":\"UserName\",\n" +
                    "      \"field_type\":\"input\",\n" +
                    "      \"keyboard\":\"text\",\n" +
                    "      \"required\":false,\n" +
                    "      \"is_active\":true,\n" +
                    "      \"icon\":\"https://jemala.png\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"field_id\":2,\n" +
                    "      \"hint\":\"Email\",\n" +
                    "      \"field_type\":\"input\",\n" +
                    "      \"required\":true,\n" +
                    "      \"keyboard\":\"text\",\n" +
                    "      \"is_active\":true,\n" +
                    "      \"icon\":\"https://jemala.png\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"field_id\":3,\n" +
                    "      \"hint\":\"phone\",\n" +
                    "      \"field_type\":\"input\",\n" +
                    "      \"required\":true,\n" +
                    "      \"keyboard\":\"number\",\n" +
                    "      \"is_active\":true,\n" +
                    "      \"icon\":\"https://jemala.png\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  [\n" +
                    "    {\n" +
                    "      \"field_id\":4,\n" +
                    "      \"hint\":\"FullName\",\n" +
                    "      \"field_type\":\"input\",\n" +
                    "      \"keyboard\":\"text\",\n" +
                    "      \"required\":true,\n" +
                    "      \"is_active\":true,\n" +
                    "      \"icon\":\"https://jemala.png\" },\n" +
                    "    {\n" +
                    "      \"field_id\":14,\n" +
                    "      \"hint\":\"Jemali\",\n" +
                    "      \"field_type\":\"input\",\n" +
                    "      \"keyboard\":\"text\",\n" +
                    "      \"required\":false,\n" +
                    "      \"is_active\":true,\n" +
                    "      \"icon\":\"https://jemala.png\" },\n" +
                    "    {\n" +
                    "      \"field_id\":89,\n" +
                    "      \"hint\":\"Birthday\",\n" +
                    "      \"field_type\":\"chooser\",\n" +
                    "      \"required\":false,\n" +
                    "      \"is_active\":true,\n" +
                    "      \"icon\":\"https://jemala.png\" },\n" +
                    "    {\n" +
                    "      \"field_id\":898,\n" +
                    "      \"hint\":\"Gender\",\n" +
                    "      \"field_type\":\"chooser\",\n" +
                    "      \"required\":\"false\",\n" +
                    "      \"is_active\":true,\n" +
                    "      \"icon\":\"https://jemala.png\" }\n" +
                    "  ]\n" +
                    "]\n"
        val gson = Gson()
        val inputs = object : TypeToken<List<List<InputField>>>() {}.type
        return gson.fromJson(jsonData, inputs)
    }
}

