package com.task.ubbar.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.ubbar.data.model.AddressRequestModel
import com.task.ubbar.data.model.AddressResponseModel
import com.task.ubbar.data.model.NetworkResult
import com.task.ubbar.domain.usecase.AddAddressUseCase
import com.task.ubbar.domain.usecase.GetAddressesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class UbbarViewModel @Inject constructor(
    private val addAddressUseCase: AddAddressUseCase,
    private val getAddressesUseCase: GetAddressesUseCase
) : ViewModel() {
    private val _setAddressResponse = MutableLiveData<NetworkResult<AddressResponseModel>>()
    val setAddressResponse: LiveData<NetworkResult<AddressResponseModel>> = _setAddressResponse

    private val _getAddressResponse = MutableLiveData<NetworkResult<List<AddressResponseModel>?>>()
    val getAddressResponse: LiveData<NetworkResult<List<AddressResponseModel>?>> =
        _getAddressResponse

    var selectPoint: GeoPoint? = null
    var address: String = ""
    var phone: String = ""
    var landLine: String = ""
    var name: String = ""
    var lastName: String = ""
    val gender = "Male"

    fun setAddress() {
        viewModelScope.launch {
            val addressRequestModel = AddressRequestModel(
                address = address,
                lat = selectPoint?.latitude?.toLong() ?: 0,
                lng = selectPoint?.longitude?.toLong() ?: 0,
                coordiante_mobile = phone,
                coordiante_phone_number = landLine,
                first_name = name,
                last_name = lastName,
                gender = gender
            )
            addAddressUseCase.invoke(addressRequestModel).collectLatest { result ->
                Log.d("Obbbaaaar", "setAddress: $result")
                _setAddressResponse.value = result
            }
        }
    }

    fun getAddress() {
        viewModelScope.launch {
           getAddressesUseCase.invoke().collectLatest { result ->
                Log.d("Obbbaaaar", "getAddress: $result")
                _getAddressResponse.value = result
            }
        }
    }
}