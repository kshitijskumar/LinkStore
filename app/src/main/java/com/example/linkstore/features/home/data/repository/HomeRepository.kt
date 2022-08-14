package com.example.linkstore.features.home.data.repository

import com.example.linkstore.features.home.data.local.IHomeLocalDataSource
import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.features.savelink.data.toLinkAppModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class HomeRepository @Inject constructor(
    private val localDataSource: IHomeLocalDataSource
) : IHomeRepository {

    override fun getAllLinks(): Flow<List<LinkAppModel>> {
        return localDataSource.getAllLinks().map { list ->
            list.map { it.toLinkAppModel() }
        }
    }
}