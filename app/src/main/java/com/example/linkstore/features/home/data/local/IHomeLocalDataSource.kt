package com.example.linkstore.features.home.data.local

import com.example.linkstore.features.savelink.data.models.entities.LinkEntity
import kotlinx.coroutines.flow.Flow

interface IHomeLocalDataSource {

    fun getAllLinks(): Flow<List<LinkEntity>>

    fun getAllLinksForGroupNameAsFlow(groupName: String): Flow<List<LinkEntity>>
    suspend fun deleteLink(link: String)

}