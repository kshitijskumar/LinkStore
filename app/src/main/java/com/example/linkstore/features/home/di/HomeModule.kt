package com.example.linkstore.features.home.di

import com.example.linkstore.features.home.data.local.HomeLocalDataSource
import com.example.linkstore.features.home.data.local.IHomeLocalDataSource
import com.example.linkstore.features.home.data.repository.HomeRepository
import com.example.linkstore.features.home.data.repository.IHomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class HomeModule {

    @Binds
    abstract fun provideHomeLocalDataSource(impl: HomeLocalDataSource): IHomeLocalDataSource

    @Binds
    abstract fun provideHomeRepository(impl: HomeRepository): IHomeRepository


}