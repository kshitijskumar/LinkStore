package com.example.linkstore.features.linkprocessor

import com.example.linkstore.common.utils.ResultData


fun ResultData<ProcessedLinkData, LinkProcessorError>.shouldNavigateToSaveLinkFlow(): Boolean {
    return (this is ResultData.Success) || (this is ResultData.Error && this.error != LinkProcessorError.NO_LINK)
}