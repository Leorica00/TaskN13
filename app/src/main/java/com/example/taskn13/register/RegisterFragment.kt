package com.example.taskn13.register

import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskn13.BaseFragment
import com.example.taskn13.FieldsRecyclerAdapter
import com.example.taskn13.databinding.FragmentRegisterBinding
import com.example.taskn13.register.model.FieldsViewModel
import com.example.taskn13.register.model.InputField
import com.example.taskn13.register.model.Inputs

class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private lateinit var data: List<List<InputField>>
    private lateinit var adapter: FieldsRecyclerAdapter
    private val viewModel: FieldsViewModel by viewModels()
    private lateinit var listOfData: List<InputField>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = viewModel.parseJson(requireContext())
        listOfData = data.flatten()
    }

    override fun setUp() {
        setUpRecycler()
    }

    override fun setUpListeners() {
        onRegisterButtonClick()
    }

    private fun setUpRecycler() {
        binding.inputRecycler.layoutManager = LinearLayoutManager(context)
        binding.inputRecycler.adapter = FieldsRecyclerAdapter().apply {
            adapter = this
            setData(data)
        }
    }

    private fun onRegisterButtonClick() {
        binding.registerBtn.setOnClickListener {
            val inputs: MutableMap<Int, Inputs> = adapter.getMapOfInputs()
            val requiredInputs = inputs.filter { (_, input) -> input.isRequired }
            if (validateFields(requiredInputs)) {
                val finalMap = inputs.mapValues { it.value.inputValue }
                Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show()
                d("finalMapValues", finalMap.toString())
            }
        }
    }

    private fun validateFields(listOfInputs: Map<Int, Inputs>): Boolean {
        listOfInputs.forEach {(id, input) ->
            if (input.inputValue.trim() == "") {
                val hint = listOfData.find { it.fieldId == id }?.hint
                Toast.makeText(context, "Fill All Fields - $hint is missing", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}

