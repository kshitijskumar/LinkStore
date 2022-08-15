package com.example.linkstore.features.savelink.data.models.appmodel

sealed class SaveScreenInitialData(
    val originalLink: String,
    val groupName: String,
    val timeStamp: Long
) {

    class FreshData(
        originalLink: String,
        groupName: String,
        timeStamp: Long
    ): SaveScreenInitialData(originalLink, groupName, timeStamp)

    class ExistingData(
        originalLink: String,
        groupName: String,
        timeStamp: Long,
        val extraNote: String?,
        val thumbnailUrl: String
    ): SaveScreenInitialData(originalLink, groupName, timeStamp)
}
