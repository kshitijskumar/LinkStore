package com.example.linkstore.features.alllinksofgroup.domain

import com.example.linkstore.features.home.data.repository.IHomeRepository
import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetAllLinksForGroupNameUseCase @Inject constructor(
    private val homeRepository: IHomeRepository
){

    operator fun invoke(groupName: String): Flow<List<LinkAppModel>> {
        return homeRepository.getAllLinksForGroupNameAsFlow(groupName = groupName)
    }

}