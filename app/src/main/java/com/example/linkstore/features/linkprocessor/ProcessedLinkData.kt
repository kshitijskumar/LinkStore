package com.example.linkstore.features.linkprocessor

data class ProcessedLinkData(
    val originalLink: String,
    val groupNow: String,
    val timeStamp: Long
)

enum class LinkProcessorError {
    INVALID_LINK, INVALID_HOST
}