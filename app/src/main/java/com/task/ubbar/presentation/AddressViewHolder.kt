package com.task.ubbar.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.task.ubbar.R
import com.task.ubbar.data.model.AddressResponseModel

class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var address: TextView = view.findViewById(R.id.address)
    private var name: TextView = view.findViewById(R.id.name)
    private var phoneNumber: TextView = view.findViewById(R.id.phoneNumber)

    fun bind(addressModel: AddressResponseModel) {
        address.text = addressModel.address
        name.text ="${addressModel.first_name} ${addressModel.last_name}"
        phoneNumber.text = addressModel.coordiante_mobile
    }
}