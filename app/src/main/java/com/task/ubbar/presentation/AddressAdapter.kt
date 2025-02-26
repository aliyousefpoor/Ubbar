package com.task.ubbar.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.task.ubbar.R
import com.task.ubbar.data.model.AddressResponseModel

class AddressAdapter : RecyclerView.Adapter<AddressViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<AddressResponseModel>() {
        override fun areItemsTheSame(oldItem: AddressResponseModel, newItem: AddressResponseModel):
                Boolean {
            return oldItem.first_name == newItem.first_name
        }

        override fun areContentsTheSame(oldItem: AddressResponseModel, newItem: AddressResponseModel):
                Boolean {
            return oldItem == newItem
        }
    }
    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun setAddressList(list: List<AddressResponseModel>) {
        asyncListDiffer.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_item, parent, false)

        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }
}