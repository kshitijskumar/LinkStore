package com.example.linkstore.features.alllinksofgroup.domain

import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@ViewModelScoped
class GetSearchQueryRelatedLinksUseCase @Inject constructor(
    private val getAllLinksForGroupNameUseCase: GetAllLinksForGroupNameUseCase
) {

    suspend operator fun invoke(groupName: String, searchQuery: String): Flow<List<LinkAppModel>> {
        return getAllLinksForGroupNameUseCase.invoke(groupName)
            .transform {
                if (searchQuery.isEmpty()) {
                    emit(it)
                } else {
                    val searchQuerySplit = searchQuery.split(" ").filter { it.isNotEmpty() }
                    val emittedSet = mutableSetOf<LinkAppModel>()
                    searchQuerySplit.forEach { split ->
                        val linksContainingThisSplit = it.filter { it.extraNote?.contains(split, ignoreCase = true) == true }
                        emittedSet.addAll(linksContainingThisSplit)
                        emit(emittedSet.toList())
                    }
                }
            }
    }

}