package com.example.linkstore.features.alllinksofgroup.domain

import com.example.linkstore.features.home.data.repository.IHomeRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DeleteLinkUseCase @Inject constructor(
    private val homeRepository: IHomeRepository
){

    suspend operator fun invoke(link: String) {
        homeRepository.deleteLink(link)
    }

}