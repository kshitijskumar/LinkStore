package com.example.linkstore.features.home.domain

import com.example.linkstore.features.home.data.models.GroupNameAppModel
import com.example.linkstore.features.home.data.repository.IHomeRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ViewModelScoped
class GetDistinctGroupNamesUseCase @Inject constructor(
    private val homeRepository: IHomeRepository
) {

    operator fun invoke(): Flow<List<GroupNameAppModel>> {
        return homeRepository.getAllLinks().flatMapLatest {
            val groupNamesAndCountMap = mutableMapOf<String, Int>()
            it.forEach { link ->
                val currentCountForGroup = groupNamesAndCountMap[link.groupName] ?: 0
                val updatedCount = currentCountForGroup + 1
                groupNamesAndCountMap[link.groupName] = updatedCount
            }
            val result = groupNamesAndCountMap.map { entry ->
                GroupNameAppModel(
                    groupName = entry.key,
                    countOfLinksInGroup = entry.value
                )
            }
            flowOf(result)
        }
    }

}