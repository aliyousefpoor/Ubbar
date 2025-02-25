package com.task.ubbar.domain.usecase

import com.task.ubbar.data.model.AddressResponseModel
import com.task.ubbar.data.model.NetworkResult
import com.task.ubbar.domain.repository.UbbarRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAddressesUseCase @Inject constructor(private val repository: UbbarRepository) {
    suspend fun invoke(): Flow<NetworkResult<List<AddressResponseModel>>> {
        return repository.getAddresses()
    }
}