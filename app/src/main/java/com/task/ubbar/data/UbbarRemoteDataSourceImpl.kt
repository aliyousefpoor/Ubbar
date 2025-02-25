package com.task.ubbar.data

import com.task.ubbar.data.service.UbbarService
import javax.inject.Inject

class UbbarRemoteDataSourceImpl @Inject constructor(private val service: UbbarService) :
    UbbarRemoteDataSource {
}