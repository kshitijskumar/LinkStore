package com.example.linkstore.features.savelink

import com.example.linkstore.features.savelink.data.models.SaveScreenFlow
import com.example.linkstore.features.savelink.data.models.appmodel.SaveScreenInitialData
import com.example.linkstore.features.savelink.data.toSaveScreenFlow
import com.example.linkstore.mvi.BasePartialChange


data class SaveLinkState(
    val originalLink: String = "",
    val groupName: String = "",
    val timestamp: Long = -1,
    val extraNote: String? = null,
    val thumbnailUrl: String = "",
    val recentGroupNameSuggestions: List<String> = listOf(),
    val isSaveButtonsEnabled: Boolean = false,
    val saveScreenFlow: SaveScreenFlow = SaveScreenFlow.FRESH_LINK
)

sealed class SaveLinkIntent {
    data class InitializationIntent(val initialData: SaveScreenInitialData) : SaveLinkIntent()
    data class OnGroupNameUpdate(val newGroupName: String) : SaveLinkIntent()
    data class OnExtraNoteUpdate(val newExtraNote: String) : SaveLinkIntent()
    data class OnGroupNameSuggestionClicked(val clickedGroupName: String) : SaveLinkIntent()
    object SaveAndGotoHomeClicked : SaveLinkIntent()
    object SaveAndExitClicked : SaveLinkIntent()
    data class UpdateExtraNoteIfNecessaryAndThumbnail(val extraNote: String, val thumbnailUrl: String): SaveLinkIntent()
}

sealed class SaveLinkSideEffect {
    object GotoHomeScreen : SaveLinkSideEffect()
    object ExitApp : SaveLinkSideEffect()
}

sealed class SaveLinkPartialChange : BasePartialChange<SaveLinkState> {

    data class InitializationChange(
        val data: SaveScreenInitialData,
        val recentGroupNames: List<String>
    ): SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkState): SaveLinkState {
            val extraNote = when(data) {
                is SaveScreenInitialData.ExistingData -> data.extraNote
                is SaveScreenInitialData.FreshData -> "" // no note when link is freshly processed
            }
            val thumbnailUrl = when(data) {
                is SaveScreenInitialData.ExistingData -> data.thumbnailUrl
                is SaveScreenInitialData.FreshData -> "" // no thumbnailUrl when link is freshly processed
            }
            return oldState.copy(
                originalLink = data.originalLink,
                groupName = data.groupName,
                timestamp = data.timeStamp,
                extraNote = extraNote,
                thumbnailUrl = thumbnailUrl,
                recentGroupNameSuggestions = recentGroupNames,
                isSaveButtonsEnabled = true,
                saveScreenFlow = data.toSaveScreenFlow()
            )
        }
    }

    sealed class OnGroupNameUpdateChange : SaveLinkPartialChange() {

        override fun reduce(oldState: SaveLinkState): SaveLinkState {
            return when(this) {
                is InvalidGroupName -> {
                    oldState.copy(
                        groupName = invalidGroupName,
                        isSaveButtonsEnabled = false,
                    )
                }
                is ValidGroupName -> {
                    oldState.copy(
                        groupName = newGroupName,
                        isSaveButtonsEnabled = true
                    )
                }
            }
        }

        data class InvalidGroupName(val invalidGroupName: String) : OnGroupNameUpdateChange()
        data class ValidGroupName(val newGroupName: String) : OnGroupNameUpdateChange()
    }

    data class OnExtraNoteUpdate(val newExtraNote: String) : SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkState): SaveLinkState {
            return oldState.copy(extraNote = newExtraNote)
        }
    }

    data class OnSuggestedGroupNameClickedChange(val clickedGroupName: String) : SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkState): SaveLinkState {
            return oldState.copy(groupName = clickedGroupName)
        }
    }

    object SaveAndGotoHomeScreenChange : SaveLinkPartialChange()

    object SaveAndExitAppChange : SaveLinkPartialChange()

    data class UpdateExtraNoteAndThumbnailChange(val extraNoteFetched: String, val thumbnailUrl: String) : SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkState): SaveLinkState {
            val extraNoteToUpdate = if (oldState.extraNote.isNullOrEmpty()) {
                extraNoteFetched
            } else {
                oldState.extraNote
            }
            return oldState.copy(
                extraNote = extraNoteToUpdate,
                thumbnailUrl = thumbnailUrl
            )
        }
    }

}