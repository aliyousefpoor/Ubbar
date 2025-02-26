package com.task.ubbar.data

import com.task.ubbar.data.model.AddressRequestModel
import com.task.ubbar.data.model.AddressResponseModel
import com.task.ubbar.data.model.NetworkResult
import com.task.ubbar.data.service.UbbarService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UbbarRemoteDataSourceImpl @Inject constructor(private val service: UbbarService) :
    UbbarRemoteDataSource {
    override suspend fun fetchAddress(addressRequestModel: AddressRequestModel): Flow<NetworkResult<AddressResponseModel>> =
        flow {
            emit(NetworkResult.Loading())
            try {
                val result = service.fetchAddress(addressRequestModel)
                if (result.isSuccessful) {
                    result.body()?.let {
                        emit(NetworkResult.Success(it))
                    }
                } else {
                    emit(
                        NetworkResult.HttpException(
                            errorCode = result.code(),
                            errorMessage = result.errorBody()?.string() ?: result.message()
                        )
                    )
                }
            } catch (e: Exception) {
                emit(NetworkResult.NetworkError(e))
            }
        }

    override suspend fun getAddresses(): Flow<NetworkResult<List<AddressResponseModel>>> = flow {
        emit(NetworkResult.Loading())
        try {
            val result = service.getAddress()
            if (result.isSuccessful) {
                result.body()?.let {
                    emit(NetworkResult.Success(it.subList(0, 30)))
                }
            } else {
                emit(
                    NetworkResult.HttpException(
                        errorCode = result.code(),
                        errorMessage = result.errorBody()?.string() ?: result.message()
                    )
                )
            }
        } catch (e: Exception) {
            emit(NetworkResult.NetworkError(e))
        }
    }
}