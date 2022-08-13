package com.example.linkstore.features.linkprocessor

import android.net.Uri
import com.example.linkstore.common.utils.ResultData
import javax.inject.Inject

interface ILinkProcessor {
    fun processLink(originalLink: String?): ResultData<ProcessedLinkData, LinkProcessorError>
}

class LinkProcessor @Inject constructor() : ILinkProcessor {

    override fun processLink(originalLink: String?): ResultData<ProcessedLinkData, LinkProcessorError> {
        if(originalLink == null) {
            return ResultData.Error(LinkProcessorError.NO_LINK)
        }

        try {
            val uri = Uri.parse(originalLink)
            val hostName = uri.host ?: return ResultData.Error(LinkProcessorError.INVALID_HOST)
            val filteredHostName = hostName
                .replace("https://", "", ignoreCase = true)
                .replace("http://", "", ignoreCase = true)
                .replace("www.", "", ignoreCase = true)
                .replace(".com", "", ignoreCase = true)

            val currentTimeStamp = System.currentTimeMillis()

            return ResultData.Success(
                data = ProcessedLinkData(
                    originalLink = originalLink,
                    groupNow = filteredHostName,
                    timeStamp = currentTimeStamp
                )
            )
        } catch (e: Exception) {
            return ResultData.Error(LinkProcessorError.INVALID_LINK)
        }
    }

}

