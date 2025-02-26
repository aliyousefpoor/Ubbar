package com.task.ubbar.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.ubbar.utils.Utils
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

    val nameValue = MutableLiveData<String>()
    val lastNameValue = MutableLiveData<String>()
    val phoneValue = MutableLiveData("")
    val landLineValue = MutableLiveData("")
    val addressValue = MutableLiveData<String>()

    val isFormValid = MediatorLiveData<Boolean>().apply {
        val validator = {
            val name = nameValue.value.orEmpty().isNotEmpty()
            val lastName = lastNameValue.value.orEmpty().isNotEmpty()
            val phone = Utils.isValidIranianPhoneNumber(phoneValue.value.toString())
            val landLine = Utils.isValidIranLandline(landLineValue.value.toString())
            val address = addressValue.value.orEmpty().isNotEmpty()


            value = name && lastName && phone && landLine && address
        }

        addSource(nameValue) { validator() }
        addSource(lastNameValue) { validator() }
        addSource(phoneValue) { validator() }
        addSource(landLineValue) { validator() }
        addSource(addressValue) { validator() }
    }

    var selectPoint: GeoPoint? = null

    fun setAddress(addressRequestModel: AddressRequestModel) {
        viewModelScope.launch {
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