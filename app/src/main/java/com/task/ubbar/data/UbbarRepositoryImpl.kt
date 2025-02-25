package com.task.ubbar.data

import com.task.ubbar.domain.repository.UbbarRepository
import javax.inject.Inject

class UbbarRepositoryImpl @Inject constructor(private val dataSource: UbbarRemoteDataSource):UbbarRepository {}