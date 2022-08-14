package com.example.linkstore.features.savelink.domain

import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.features.savelink.data.repository.ISaveLinkRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SaveLinkUseCase @Inject constructor(
    private val saveLinkRepository: ISaveLinkRepository
) {

    suspend operator fun invoke(
        originalLink: String,
        groupName: String,
        storingTimeStamp: Long,
        extraNote: String? = null
    ) {
        val linkAppModel = LinkAppModel(
            originalLink = originalLink,
            groupName = groupName,
            storingTimeStamp = storingTimeStamp,
            extraNote = extraNote
        )
        saveLinkRepository.saveLink(linkAppModel = linkAppModel)
    }

}