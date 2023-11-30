package com.example.taskn13.registration.adapters

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_TEXT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskn13.databinding.DateChooseRecyclerItemBinding
import com.example.taskn13.databinding.InputRecyclerItemBinding
import com.example.taskn13.databinding.SpinnerItemBinding
import com.example.taskn13.registration.InnerAdapterCallback
import com.example.taskn13.registration.model.ChooserType
import com.example.taskn13.registration.model.FieldType
import com.example.taskn13.registration.model.GenderOptions
import com.example.taskn13.registration.model.InputField
import com.example.taskn13.registration.model.InputTextType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class InnerFieldsRecyclerAdapter(private val callback: InnerAdapterCallback) : ListAdapter<InputField, ViewHolder>(
    MyItemDiffCallback()
) {

    companion object InputTypes {
        const val INPUT = 1
        const val CHOOSER_SPINNER = 2
        const val CHOOSER_DATE = 3
    }

    fun setData(list: List<InputField>) {
        submitList(list)
    }

    fun notifyInnerDataChanged(updatedInputField: InputField) {
        callback.onInnerDataChanged(updatedInputField)
    }

    inner class InputViewHolder(private val binding: InputRecyclerItemBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            val myItem = currentList[adapterPosition]
            with(binding.inputEt) {
                setText(myItem.inputValue)
                hint = myItem.hint

                inputType = when (myItem.keyboard) {
                    InputTextType.NUMBER.text -> TYPE_CLASS_NUMBER
                    else -> TYPE_CLASS_TEXT
                }

                doAfterTextChanged {
                    currentList[adapterPosition].inputValue = it.toString()
                    notifyInnerDataChanged(currentList[adapterPosition])
                }
            }
        }

    }

    inner class ChooserViewHolder(private val binding: SpinnerItemBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            binding.spinnerOptionTv.text = currentList[adapterPosition].inputValue
            val options = arrayOf(currentList[adapterPosition].hint, GenderOptions.MALE.gender, GenderOptions.FEMALE.gender)
            with(binding.spinner) {
                adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, options).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }

                onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val selectedOption = options[position]
                            binding.spinnerOptionTv.text = selectedOption
                            currentList[adapterPosition].inputValue = selectedOption
                            notifyInnerDataChanged(currentList[adapterPosition])
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
            }
        }
    }

    inner class DateChooserViewHolder(private val binding: DateChooseRecyclerItemBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            if (currentList[adapterPosition].inputValue != "")
                binding.dateTv.text = currentList[adapterPosition].inputValue

            binding.root.setOnClickListener {
                showDatePickerDialog(itemView.context, binding.dateTv)
            }
        }

        private fun showDatePickerDialog(
            context: Context,
            textView: AppCompatTextView
        ) {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                context, { _, selectedYear, monthOfYear, dayOfMonth ->
                    val selectedDateCalendar = Calendar.getInstance()
                    selectedDateCalendar.set(selectedYear, monthOfYear, dayOfMonth)
                    val selectedDate = selectedDateCalendar.time
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate)
                    textView.text = formattedDate
                    currentList[adapterPosition].inputValue = formattedDate
                    notifyInnerDataChanged(currentList[adapterPosition])
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            INPUT -> InputViewHolder(
                InputRecyclerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            CHOOSER_DATE -> DateChooserViewHolder(
                DateChooseRecyclerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            CHOOSER_SPINNER -> ChooserViewHolder(
                SpinnerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is InputViewHolder -> holder.bind()
            is DateChooserViewHolder -> holder.bind()
            is ChooserViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return with(currentList[position]) {
            if (fieldType == FieldType.INPUT.type) INPUT
            else if(fieldType == FieldType.CHOOSER.type && hint == ChooserType.DATE.hint) CHOOSER_DATE
            else if(fieldType == FieldType.CHOOSER.type && hint == ChooserType.SPINNER.hint) CHOOSER_SPINNER
            else -1
        }
    }

    private class MyItemDiffCallback : DiffUtil.ItemCallback<InputField>() {

        override fun areItemsTheSame(oldItem: InputField, newItem: InputField): Boolean {
            return oldItem.fieldId == newItem.fieldId
        }

        override fun areContentsTheSame(oldItem: InputField, newItem: InputField): Boolean {
            return oldItem == newItem
        }
    }
}