package com.example.linkstore.features.savelink.data.repository

import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel

interface ISaveLinkRepository {

    suspend fun getRecentXNumberOfGroupNames(count: Int): List<String>

    suspend fun saveLink(linkAppModel: LinkAppModel)

}