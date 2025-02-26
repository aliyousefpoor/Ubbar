package com.task.ubbar.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.task.ubbar.data.UbbarRemoteDataSource
import com.task.ubbar.data.UbbarRemoteDataSourceImpl
import com.task.ubbar.data.UbbarRepositoryImpl
import com.task.ubbar.data.service.UbbarService
import com.task.ubbar.domain.repository.UbbarRepository
import com.task.ubbar.domain.usecase.AddAddressUseCase
import com.task.ubbar.domain.usecase.GetAddressesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UbbarModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val gson: Gson? = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)  // Increase connection timeout
            .readTimeout(60, TimeUnit.SECONDS)     // Increase read timeout
            .writeTimeout(60, TimeUnit.SECONDS)    // Increase write timeout
            .retryOnConnectionFailure(true)
            .addInterceptor(ApiKeyInterceptor())
            .build()


        return Retrofit.Builder().baseUrl("https://stage.achareh.ir/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory()).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }


    @Singleton
    @Provides
    fun provideUbbarService(retrofit: Retrofit): UbbarService {
        return retrofit.create(UbbarService::class.java)
    }

    @Singleton
    @Provides
    fun provideUbbarRemoteDataSource(obarService: UbbarService): UbbarRemoteDataSourceImpl {
        return UbbarRemoteDataSourceImpl(obarService)
    }

    @Singleton
    @Provides
    fun provideUbbarRepository(
        dataSource: UbbarRemoteDataSource
    ): UbbarRepositoryImpl {
        return UbbarRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun provideAddAddressUseCase(
        repository: UbbarRepository
    ): AddAddressUseCase {
        return AddAddressUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAddressesUseCase(
        repository: UbbarRepository
    ): GetAddressesUseCase {
        return GetAddressesUseCase(repository)
    }
}