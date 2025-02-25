package com.task.ubbar.di

import com.task.ubbar.data.UbbarRemoteDataSource
import com.task.ubbar.data.UbbarRemoteDataSourceImpl
import com.task.ubbar.data.UbbarRepositoryImpl
import com.task.ubbar.domain.repository.UbbarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {
    @Binds
    abstract fun bindUbbarRemoteDataSource(
        ubbarRemoteDataSourceImpl: UbbarRemoteDataSourceImpl
    ): UbbarRemoteDataSource

    @Binds
    abstract fun bindUbbarRepository(
        ubbarRepositoryImpl: UbbarRepositoryImpl
    ): UbbarRepository
}