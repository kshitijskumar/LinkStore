package com.example.linkstore.features.linkprocessor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProcessedLinkData(
    val originalLink: String,
    val groupName: String,
    val timeStamp: Long
) : Parcelable

enum class LinkProcessorError {
    INVALID_LINK, INVALID_HOST, NO_LINK
}