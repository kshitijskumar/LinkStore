package com.example.linkstore.features.savelink.data.local.datasource

import com.example.linkstore.features.savelink.data.models.entities.LinkEntity

interface ISaveLinkLocalDataSource {

    suspend fun getRecentXNumberOfGroupNames(count: Int): List<String>

    suspend fun saveLink(linkEntity: LinkEntity)
}