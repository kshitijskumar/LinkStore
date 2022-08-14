package com.example.linkstore.features.savelink.data.models.appmodel

data class LinkAppModel(
    val originalLink: String,
    val groupName: String,
    val storingTimeStamp: Long,
    val extraNote: String?
)
