package com.example.linkstore.features.savelink.di

import android.content.Context
import com.example.linkstore.features.savelink.data.local.datasource.ISaveLinkLocalDataSource
import com.example.linkstore.features.savelink.data.local.datasource.SaveLinkLocalDataSource
import com.example.linkstore.features.savelink.data.repository.ISaveLinkRepository
import com.example.linkstore.features.savelink.data.repository.SaveLinkRepository
import com.example.linkstore.features.savelink.domain.FetchLinkPreviewDataUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ViewModelComponent::class)
@Module
object SaveLinkModule {

    @Provides
    fun provideLocalDataSource(impl: SaveLinkLocalDataSource): ISaveLinkLocalDataSource = impl

    @Provides
    fun provideRepository(impl: SaveLinkRepository): ISaveLinkRepository = impl

    @Provides
    fun provideFetchLinkPreviewDataUseCase(
        @ApplicationContext context: Context
    ): FetchLinkPreviewDataUseCase = FetchLinkPreviewDataUseCase(context = context)
}