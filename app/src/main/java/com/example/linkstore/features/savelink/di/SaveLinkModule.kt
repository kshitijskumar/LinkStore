package com.example.linkstore.features.savelink.di

import com.example.linkstore.features.savelink.data.local.datasource.ISaveLinkLocalDataSource
import com.example.linkstore.features.savelink.data.local.datasource.SaveLinkLocalDataSource
import com.example.linkstore.features.savelink.data.repository.ISaveLinkRepository
import com.example.linkstore.features.savelink.data.repository.SaveLinkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object SaveLinkModule {

    @Provides
    fun provideLocalDataSource(impl: SaveLinkLocalDataSource): ISaveLinkLocalDataSource = impl

    @Provides
    fun provideRepository(impl: SaveLinkRepository): ISaveLinkRepository = impl

}