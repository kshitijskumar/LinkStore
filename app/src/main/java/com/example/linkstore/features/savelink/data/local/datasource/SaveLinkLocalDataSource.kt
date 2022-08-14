package com.example.linkstore.features.savelink.data.local.datasource

import com.example.linkstore.features.savelink.data.local.dao.LinkDao
import com.example.linkstore.features.savelink.data.models.entities.LinkEntity
import javax.inject.Inject

class SaveLinkLocalDataSource @Inject constructor(
    private val linkDao: LinkDao
) : ISaveLinkLocalDataSource {

    override suspend fun getRecentXNumberOfGroupNames(count: Int): List<String> {
        return linkDao.getRecentXNumberOfGroupName(count)
    }

    override suspend fun saveLink(linkEntity: LinkEntity) {
        linkDao.insertLink(linkEntity = linkEntity)
    }
}