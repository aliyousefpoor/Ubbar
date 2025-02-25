package com.task.ubbar.domain.usecase

import com.task.ubbar.domain.repository.UbbarRepository
import javax.inject.Inject

class AddAddressUseCase @Inject constructor(private val  repository: UbbarRepository) {
    suspend fun invoke(){

    }
}