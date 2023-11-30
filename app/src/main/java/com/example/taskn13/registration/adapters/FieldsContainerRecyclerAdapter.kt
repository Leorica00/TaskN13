package com.example.taskn13.registration.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskn13.databinding.InputsGroupRecyclerItemBinding
import com.example.taskn13.registration.InnerAdapterCallback
import com.example.taskn13.registration.model.InputField

class FieldsContainerRecyclerAdapter(private val innerAdapterCallback: InnerAdapterCallback) :
    ListAdapter<List<InputField>, FieldsContainerRecyclerAdapter.OuterViewHolder>(
        MyItemDiffCallback()
    ), InnerAdapterCallback {

    fun setData(data: List<List<InputField>>) {
        submitList(data)
    }

    override fun onInnerDataChanged(updatedInputField: InputField) {
        innerAdapterCallback.onInnerDataChanged(updatedInputField)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OuterViewHolder {
        return OuterViewHolder(
            InputsGroupRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            innerAdapterCallback
        )
    }

    override fun onBindViewHolder(holder: OuterViewHolder, position: Int) {
        holder.bind()
    }

    inner class OuterViewHolder(
        private val binding: InputsGroupRecyclerItemBinding,
        private val innerAdapterCallback: InnerAdapterCallback
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            val adapter = InnerFieldsRecyclerAdapter(innerAdapterCallback)
            binding.containerRecycler.layoutManager = LinearLayoutManager(itemView.context)
            binding.containerRecycler.adapter = adapter
            adapter.setData(currentList[adapterPosition])
        }
    }
    private class MyItemDiffCallback : DiffUtil.ItemCallback<List<InputField>>() {

        override fun areItemsTheSame(
            oldItem: List<InputField>,
            newItem: List<InputField>
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: List<InputField>,
            newItem: List<InputField>
        ): Boolean {
            return oldItem == newItem
        }
    }
}