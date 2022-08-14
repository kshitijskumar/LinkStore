package com.example.linkstore.features.savelink.di

import com.example.linkstore.features.savelink.data.local.datasource.ISaveLinkLocalDataSource
import com.example.linkstore.features.savelink.data.local.datasource.SaveLinkLocalDataSource
import com.example.linkstore.features.savelink.data.repository.ISaveLinkRepository
import com.example.linkstore.features.savelink.data.repository.SaveLinkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
abstract class SaveLinkModule {

    @Binds
    abstract fun provideLocalDataSource(impl: SaveLinkLocalDataSource): ISaveLinkLocalDataSource

    @Binds
    abstract fun provideRepository(impl: SaveLinkRepository): ISaveLinkRepository
}