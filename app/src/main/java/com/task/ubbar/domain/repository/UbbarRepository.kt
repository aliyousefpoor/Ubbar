package com.task.ubbar.domain.repository

import com.task.ubbar.data.model.AddressRequestModel
import com.task.ubbar.data.model.AddressResponseModel
import com.task.ubbar.data.model.NetworkResult
import kotlinx.coroutines.flow.Flow

interface UbbarRepository {
    suspend fun fetchAddress(addressRequestModel: AddressRequestModel): Flow<NetworkResult<AddressResponseModel>>
    suspend fun getAddresses(): Flow<NetworkResult<List<AddressResponseModel>>>
}