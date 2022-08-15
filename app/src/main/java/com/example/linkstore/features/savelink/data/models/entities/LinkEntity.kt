package com.example.linkstore.features.savelink.data.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "links_table")
data class LinkEntity(
    @PrimaryKey(autoGenerate = false)
    val originalLink: String,
    val groupName: String,
    val storingTimeStamp: Long,
    val extraNote: String?,
    val previewThumbnail: String
)
