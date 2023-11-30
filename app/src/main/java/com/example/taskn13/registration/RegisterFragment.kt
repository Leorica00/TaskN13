package com.example.taskn13.registration

import android.util.Log.d
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskn13.BaseFragment
import com.example.taskn13.databinding.FragmentRegisterBinding
import com.example.taskn13.registration.adapters.FieldsContainerRecyclerAdapter
import com.example.taskn13.registration.model.InputField

class RegisterFragment :
    BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate),
    InnerAdapterCallback {

    private lateinit var adapter: FieldsContainerRecyclerAdapter
    private val viewModel: FieldsViewModel by viewModels()
    override fun setUp() {
        setUpRecycler()
    }
    override fun onInnerDataChanged(updatedInputField: InputField) {
        viewModel.updateInputField(updatedInputField)
    }

    override fun setUpListeners() {
        onRegisterButtonClick()
    }

    private fun setUpRecycler() {
        binding.inputRecycler.layoutManager = LinearLayoutManager(context)
        adapter = FieldsContainerRecyclerAdapter(this).apply {
            binding.inputRecycler.adapter = this
            setData(viewModel.fieldsData.value.orEmpty())
        }
    }

    private fun onRegisterButtonClick() {
        binding.registerBtn.setOnClickListener {
            val fieldsData = viewModel.fieldsData.value.orEmpty().flatten()
            val requiredInputs = fieldsData.filter { it.required }
            if (validateFields(requiredInputs)) {
                val finalMap: Map<Int, String> =
                    fieldsData.associate { it.fieldId to it.inputValue }
                Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show()
                d("finalMapValues", finalMap.toString())
            }
        }
    }

    private fun validateFields(listOfInputs: List<InputField>): Boolean {
        listOfInputs.forEach {
            if (it.inputValue == "") {
                Toast.makeText(
                    context,
                    "Fill All Fields - ${it.hint} is missing",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        }
        return true
    }
}

