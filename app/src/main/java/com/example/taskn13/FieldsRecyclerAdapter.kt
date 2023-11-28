package com.example.taskn13

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskn13.databinding.DateChooseRecyclerItemBinding
import com.example.taskn13.databinding.InputRecyclerItemBinding
import com.example.taskn13.databinding.InputsGroupRecyclerItemBinding
import com.example.taskn13.databinding.SpinnerItemBinding
import com.example.taskn13.register.model.InputField
import com.example.taskn13.register.model.Inputs
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FieldsRecyclerAdapter : ListAdapter<List<InputField>, FieldsRecyclerAdapter.OuterViewHolder>(MyItemDiffCallback()) {

    private val mapOfInputs = mutableMapOf<Int, Inputs>()

    fun setData(data: List<List<InputField>>) {
        submitList(data)
    }

    fun getMapOfInputs(): MutableMap<Int, Inputs> {
        return mapOfInputs
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        return OuterViewHolder(InputsGroupRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
        holder.bind()
    }


    inner class OuterViewHolder(private val binding: InputsGroupRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val itemList = getItem(adapterPosition)
            itemList.forEach { myItem ->
                mapOfInputs[myItem.fieldId] = Inputs(myItem.required, "")
                when (myItem.fieldType) {
                    "input" -> {
                        val innerInputViewHolder = InputRecyclerItemBinding.inflate(
                            LayoutInflater.from(itemView.context),
                            binding.root,
                            false
                        )
                        innerInputViewHolder.inputEt.inputType =
                            if (myItem.keyboard == "text") InputType.TYPE_CLASS_TEXT
                            else InputType.TYPE_CLASS_NUMBER
                        with(innerInputViewHolder.inputEt) {
                            hint = SpannableStringBuilder(myItem.hint)
                            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_logo, 0
                            )
                        }
                        innerInputViewHolder.inputEt.setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) {
                                mapOfInputs[myItem.fieldId] = Inputs(
                                    myItem.required,
                                    innerInputViewHolder.inputEt.text.toString()
                                )
                            }
                        }
                        binding.root.addView(innerInputViewHolder.inputEt)
                    }

                    "chooser" -> {
                        if (myItem.hint == "Birthday") {
                            val datePicker = DateChooseRecyclerItemBinding.inflate(
                                LayoutInflater.from(itemView.context),
                                binding.root,
                                false
                            )
                            binding.root.setOnClickListener {
                                showDatePickerDialog(itemView.context, datePicker.dateTv, myItem)
                            }
                            binding.root.addView(datePicker.root)
                        } else {
                            val innerChooserViewHolder = SpinnerItemBinding.inflate(
                                LayoutInflater.from(itemView.context),
                                binding.root,
                                false
                            )
                            val options = arrayOf(myItem.hint, "Male", "Female")
                            with(innerChooserViewHolder.spinner) {
                                adapter = ArrayAdapter(
                                    itemView.context,
                                    android.R.layout.simple_spinner_item,
                                    options
                                ).apply {
                                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                }
                                onItemSelectedListener = MySpinnerCallBack(
                                    options,
                                    innerChooserViewHolder,
                                    mapOfInputs,
                                    myItem
                                )
                            }
                            binding.root.addView(innerChooserViewHolder.root)
                        }
                    }
                }

            }
        }

        private fun showDatePickerDialog(context: Context, textView: AppCompatTextView ,inputField: InputField) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context, { _, selectedYear, monthOfYear, dayOfMonth ->
                    val selectedDateCalendar = Calendar.getInstance()
                    selectedDateCalendar.set(
                        selectedYear,
                        monthOfYear,
                        dayOfMonth
                    )
                    val selectedDate = selectedDateCalendar.time
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate)
                    textView.text = formattedDate
                    mapOfInputs[inputField.fieldId] = Inputs(false, formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
    private class MyItemDiffCallback : DiffUtil.ItemCallback<List<InputField>>() {

        override fun areItemsTheSame(oldItem: List<InputField>, newItem: List<InputField>): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: List<InputField>, newItem: List<InputField>): Boolean {
            return oldItem == newItem
        }
    }

    private class MySpinnerCallBack(
        val options: Array<String>,
        val innerChooserViewHolder: SpinnerItemBinding,
        val mapOfInputs: MutableMap<Int, Inputs>,
        val myItem: InputField
    ) : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedOption = options[position]
            innerChooserViewHolder.spinnerOptionTv.text = selectedOption
            mapOfInputs[myItem.fieldId] = Inputs(false, selectedOption)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}