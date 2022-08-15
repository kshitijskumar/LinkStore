package com.example.linkstore.features.savelink.data

import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
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