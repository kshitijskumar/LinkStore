package com.example.linkstore.features.savelink.domain

import com.example.linkstore.features.savelink.data.repository.ISaveLinkRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetRecentGroupNameSuggestionsUseCase @Inject constructor(
    private val saveLinkRepository: ISaveLinkRepository
) {

    suspend operator fun invoke(numberOfSuggestionsNeeded: Int = DEFAULT_COUNT): List<String> {
        val updatedNumberIfNeeded = if (numberOfSuggestionsNeeded < 0) DEFAULT_COUNT else numberOfSuggestionsNeeded
        return saveLinkRepository.getRecentXNumberOfGroupNames(updatedNumberIfNeeded)
    }

    companion object {
        private const val DEFAULT_COUNT = 5
    }

}