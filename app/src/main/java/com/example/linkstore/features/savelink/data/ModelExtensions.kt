package com.example.linkstore.features.savelink.data

import com.example.linkstore.features.linkprocessor.ProcessedLinkData
import com.example.linkstore.features.savelink.data.models.SaveScreenFlow
import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.features.savelink.data.models.appmodel.SaveScreenInitialData
import com.example.linkstore.features.savelink.data.models.entities.LinkEntity

fun LinkEntity.toLinkAppModel(): LinkAppModel {
    return LinkAppModel(
        originalLink = originalLink,
        groupName = groupName,
        storingTimeStamp = storingTimeStamp,
        extraNote = extraNote,
        thumbnailUrl = previewThumbnail
    )
}

fun LinkAppModel.toLinkEntity(): LinkEntity {
    return LinkEntity(
        originalLink = originalLink,
        groupName = groupName,
        storingTimeStamp = storingTimeStamp,
        extraNote = extraNote,
        previewThumbnail = thumbnailUrl
    )
}

fun SaveScreenInitialData.toSaveScreenFlow(): SaveScreenFlow {
    return when(this) {
        is SaveScreenInitialData.ExistingData -> SaveScreenFlow.EDIT_LINK
        is SaveScreenInitialData.FreshData -> SaveScreenFlow.FRESH_LINK
    }
}

fun ProcessedLinkData.toSaveScreenFreshData(): SaveScreenInitialData.FreshData {
    return SaveScreenInitialData.FreshData(
        originalLink = originalLink,
        groupName = groupName,
        timeStamp = timeStamp
    )
}

fun LinkAppModel.toSaveScreenExistingData(): SaveScreenInitialData.ExistingData {
    return SaveScreenInitialData.ExistingData(
        originalLink = originalLink,
        groupName = groupName,
        timeStamp = storingTimeStamp,
        extraNote = extraNote,
        thumbnailUrl = thumbnailUrl
    )
}