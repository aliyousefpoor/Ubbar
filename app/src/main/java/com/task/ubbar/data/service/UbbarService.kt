package com.task.ubbar.data.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UbbarService {
    @POST("api/karfarmas/address")
    suspend fun fetchAddress(@Body request: Any): retrofit2.Response<Any>

    @GET("api/karfarmas/address")
    suspend fun getAddress(): retrofit2.Response<List<Any>>
}