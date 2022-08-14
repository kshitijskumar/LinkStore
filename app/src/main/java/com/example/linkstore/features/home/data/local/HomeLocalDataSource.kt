package com.example.linkstore.features.home.data.local

import com.example.linkstore.features.savelink.data.local.dao.LinkDao
import com.example.linkstore.features.savelink.data.models.entities.LinkEntity
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class HomeLocalDataSource @Inject constructor(
    private val linkDao: LinkDao
) : IHomeLocalDataSource {

    override fun getAllLinks(): Flow<List<LinkEntity>> {
        return linkDao.getAllLinksAsFlow()
    }
}