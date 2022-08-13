package com.example.linkstore.application.di

import android.content.Context
import com.example.linkstore.application.db.AppDb
import com.example.linkstore.features.linkprocessor.ILinkProcessor
import com.example.linkstore.features.linkprocessor.LinkProcessor
import com.example.linkstore.features.savelink.data.local.dao.LinkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainModule {

    @Provides
    @Singleton
    fun provideAppDb(@ApplicationContext appContext: Context): AppDb {
        return AppDb.getInstance(appContext)
    }

    @Provides
    @Singleton
    fun provideLinkDao(appDb: AppDb): LinkDao = appDb.linkDao

    @Provides
    fun provideLinkProcessor(impl: LinkProcessor): ILinkProcessor = impl

}