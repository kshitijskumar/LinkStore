package com.example.linkstore.features.savelink.data.repository

import com.example.linkstore.features.savelink.data.local.datasource.ISaveLinkLocalDataSource
import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.features.savelink.data.toLinkEntity
import javax.inject.Inject

class SaveLinkRepository @Inject constructor(
    private val localDataSource: ISaveLinkLocalDataSource
) : ISaveLinkRepository {

    override suspend fun getRecentXNumberOfGroupNames(count: Int): List<String> {
        return localDataSource.getRecentXNumberOfGroupNames(count = count)
    }

    override suspend fun saveLink(linkAppModel: LinkAppModel) {
        localDataSource.saveLink(linkAppModel.toLinkEntity())
    }
}