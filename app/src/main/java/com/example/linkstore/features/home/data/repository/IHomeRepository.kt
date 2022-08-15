package com.example.linkstore.features.home.data.repository

import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import kotlinx.coroutines.flow.Flow

interface IHomeRepository {

    fun getAllLinks(): Flow<List<LinkAppModel>>

    fun getAllLinksForGroupNameAsFlow(groupName: String): Flow<List<LinkAppModel>>

    suspend fun deleteLink(link: String)

}