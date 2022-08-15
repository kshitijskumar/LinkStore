package com.example.linkstore.features.savelink.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.linkstore.common.utils.IDispatchersProvider
import com.example.linkstore.common.utils.ResultData
import com.example.linkstore.features.savelink.SaveLinkIntent
import com.example.linkstore.features.savelink.SaveLinkPartialChange
import com.example.linkstore.features.savelink.SaveLinkSideEffect
import com.example.linkstore.features.savelink.SaveLinkState
import com.example.linkstore.features.savelink.domain.FetchLinkPreviewDataUseCase
import com.example.linkstore.features.savelink.domain.GetRecentGroupNameSuggestionsUseCase
import com.example.linkstore.features.savelink.domain.SaveLinkUseCase
import com.example.linkstore.features.savelink.domain.ValidateGroupNameUseCase
import com.example.linkstore.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveLinkViewModel @Inject constructor(
    private val dispatcherProvider: IDispatchersProvider,
    private val getRecentGroupNameSuggestionsUseCase: GetRecentGroupNameSuggestionsUseCase,
    private val validateGroupNameUseCase: ValidateGroupNameUseCase,
    private val saveLinkUseCase: SaveLinkUseCase,
    private val fetchLinkPreviewDataUseCase: FetchLinkPreviewDataUseCase
) : MviViewModel<SaveLinkState, SaveLinkIntent, SaveLinkPartialChange, SaveLinkSideEffect>() {

    override fun getInitialState(): SaveLinkState {
        return SaveLinkState()
    }

    private var linkPreviewFetchJob: Job? = null

    override fun Flow<SaveLinkIntent>.toPartialChange(): Flow<SaveLinkPartialChange> {
        return merge(
            handleInitializationIntent(filterIsInstance()),
            handleOnGroupNameUpdateIntent(filterIsInstance()),
            handleOnExtraNameUpdateIntent(filterIsInstance()),
            handleOnGroupNameSuggestedClickedIntent(filterIsInstance()),
            handleSaveAndGotoHomeClickedIntent(filterIsInstance()),
            handleSaveAndExitAppClickedIntent(filterIsInstance()),
            handleUpdateExtraNoteIfNecessaryAndThumbnailIntent(filterIsInstance())
        )
    }

    override fun getSideEffects(change: SaveLinkPartialChange): List<SaveLinkSideEffect> {
        val sideEffect: SaveLinkSideEffect? = when(change) {
            is SaveLinkPartialChange.InitializationChange -> {
                null
            }
            is SaveLinkPartialChange.OnExtraNoteUpdate -> {
                null
            }
            is SaveLinkPartialChange.OnGroupNameUpdateChange.InvalidGroupName -> {
                null
            }
            is SaveLinkPartialChange.OnGroupNameUpdateChange.ValidGroupName -> {
                null
            }
            is SaveLinkPartialChange.OnSuggestedGroupNameClickedChange -> {
                null
            }
            SaveLinkPartialChange.SaveAndExitAppChange -> {
                SaveLinkSideEffect.ExitApp
            }
            SaveLinkPartialChange.SaveAndGotoHomeScreenChange -> {
                SaveLinkSideEffect.GotoHomeScreen
            }
            is SaveLinkPartialChange.UpdateExtraNoteAndThumbnailChange -> {
                null
            }
        }

        return mutableListOf<SaveLinkSideEffect>().apply {
            sideEffect?.let { add(it) }
        }
    }

    private fun handleInitializationIntent(
        flow: Flow<SaveLinkIntent.InitializationIntent>
    ): Flow<SaveLinkPartialChange.InitializationChange> {
        return flow.map {
            val recentGroupNamed = getRecentGroupNameSuggestionsUseCase.invoke()
            startLinkPreviewFetchJob(it.processedLinkData.originalLink)
            SaveLinkPartialChange.InitializationChange(
                data = it.processedLinkData,
                recentGroupNames = recentGroupNamed
            )
        }
    }

    private fun handleOnGroupNameUpdateIntent(
        flow: Flow<SaveLinkIntent.OnGroupNameUpdate>
    ): Flow<SaveLinkPartialChange.OnGroupNameUpdateChange> {
        return flow.map {
            val isValidGroupName = validateGroupNameUseCase.invoke(it.newGroupName)
            if (isValidGroupName) {
                SaveLinkPartialChange.OnGroupNameUpdateChange.ValidGroupName(it.newGroupName)
            } else {
                SaveLinkPartialChange.OnGroupNameUpdateChange.InvalidGroupName(it.newGroupName)
            }
        }
    }

    private fun handleOnExtraNameUpdateIntent(
        flow: Flow<SaveLinkIntent.OnExtraNoteUpdate>
    ): Flow<SaveLinkPartialChange.OnExtraNoteUpdate> {
        return flow.map {
            SaveLinkPartialChange.OnExtraNoteUpdate(it.newExtraNote)
        }
    }

    private fun handleOnGroupNameSuggestedClickedIntent(
        flow: Flow<SaveLinkIntent.OnGroupNameSuggestionClicked>
    ): Flow<SaveLinkPartialChange.OnSuggestedGroupNameClickedChange> {
        return flow.map {
            SaveLinkPartialChange.OnSuggestedGroupNameClickedChange(it.clickedGroupName)
        }
    }

    private fun handleSaveAndGotoHomeClickedIntent(
        flow: Flow<SaveLinkIntent.SaveAndGotoHomeClicked>
    ): Flow<SaveLinkPartialChange.SaveAndGotoHomeScreenChange> {
        return flow.map {
            saveLinkInformation()
            SaveLinkPartialChange.SaveAndGotoHomeScreenChange
        }
    }

    private fun handleSaveAndExitAppClickedIntent(
        flow: Flow<SaveLinkIntent.SaveAndExitClicked>
    ): Flow<SaveLinkPartialChange.SaveAndExitAppChange> {
        return flow.map {
            saveLinkInformation()
            SaveLinkPartialChange.SaveAndExitAppChange
        }
    }

    private fun handleUpdateExtraNoteIfNecessaryAndThumbnailIntent(
        flow: Flow<SaveLinkIntent.UpdateExtraNoteIfNecessaryAndThumbnail>
    ): Flow<SaveLinkPartialChange.UpdateExtraNoteAndThumbnailChange> {
        return flow.map {
            SaveLinkPartialChange.UpdateExtraNoteAndThumbnailChange(
                extraNoteFetched = it.extraNote,
                thumbnailUrl = it.thumbnailUrl
            )
        }
    }

    private suspend fun saveLinkInformation() {
        with(state.value) {
            saveLinkUseCase.invoke(
                originalLink = originalLink,
                groupName = groupName,
                storingTimeStamp = timestamp,
                extraNote = extraNote
            )
        }
    }

    private fun startLinkPreviewFetchJob(link: String) {
        linkPreviewFetchJob?.cancel()
        linkPreviewFetchJob = viewModelScope.launch(dispatcherProvider.ioDispatcher) {
            fetchLinkPreviewDataUseCase.invoke(link)
                .collect {
                    when(it) {
                        is ResultData.Error -> Log.d("OpenGraphStuff", "collect error: ${it.error}")
                        is ResultData.Success -> {
                            processIntent(
                                SaveLinkIntent.UpdateExtraNoteIfNecessaryAndThumbnail(
                                    extraNote = it.data.title ?: "",
                                    thumbnailUrl = it.data.image ?: ""
                                )
                            )
                        }
                    }
                }
        }
    }

    private fun stopLinkPreviewFetchJob() {
        linkPreviewFetchJob?.cancel()
        linkPreviewFetchJob = null
    }
}