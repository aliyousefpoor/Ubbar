package com.task.ubbar.data

import com.task.ubbar.data.model.AddressRequestModel
import com.task.ubbar.data.model.AddressResponseModel
import com.task.ubbar.data.model.NetworkResult
import com.task.ubbar.domain.repository.UbbarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UbbarRepositoryImpl @Inject constructor(private val dataSource: UbbarRemoteDataSource):UbbarRepository {
    override suspend fun fetchAddress(addressRequestModel: AddressRequestModel): Flow<NetworkResult<AddressResponseModel>> {
        return withContext(Dispatchers.IO){
            dataSource.fetchAddress(addressRequestModel)
        }
    }

    override suspend fun getAddresses(): Flow<NetworkResult<List<AddressResponseModel>>> {
        return withContext(Dispatchers.IO){
            dataSource.getAddresses()
        }
    }
}