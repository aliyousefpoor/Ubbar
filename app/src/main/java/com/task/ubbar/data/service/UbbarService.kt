package com.task.ubbar.data.service

import com.task.ubbar.data.model.AddressRequestModel
import com.task.ubbar.data.model.AddressResponseModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UbbarService {
    @POST("api/karfarmas/address")
    suspend fun fetchAddress(@Body request: AddressRequestModel): retrofit2.Response<AddressResponseModel>

    @GET("api/karfarmas/address")
    suspend fun getAddress(): retrofit2.Response<List<AddressResponseModel>>
}