package com.example.linkstore.features.savelink

import com.example.linkstore.features.linkprocessor.ProcessedLinkData
import com.example.linkstore.mvi.BasePartialChange


data class SaveLinkState(
    val originalLink: String = "",
    val groupName: String = "",
    val timestamp: Long = -1,
    val extraNote: String? = null,
    val recentGroupNameSuggestions: List<String> = listOf(),
    val isSaveButtonsEnabled: Boolean = false,
    val isGroupNameIncorrect: Boolean = false
)

sealed class SaveLinkIntent {
    data class InitializationIntent(val processedLinkData: ProcessedLinkData) : SaveLinkIntent()
    data class OnGroupNameUpdate(val newGroupName: String) : SaveLinkIntent()
    data class OnExtraNoteUpdate(val newExtraNote: String) : SaveLinkIntent()
    data class OnGroupNameSuggestionClicked(val clickedGroupName: String) : SaveLinkIntent()
    object SaveAndGotoHomeClicked : SaveLinkIntent()
    object SaveAndExitClicked : SaveLinkIntent()
}

sealed class SaveLinkSideEffect {
    object GotoHomeScreen : SaveLinkSideEffect()
    object ExitApp : SaveLinkSideEffect()
}

sealed class SaveLinkPartialChange : BasePartialChange<SaveLinkState> {

    data class InitializationChange(
        val data: ProcessedLinkData,
        val recentGroupNames: List<String>
    ): SaveLinkPartialChange() {
        override fun reduce(oldState: SaveLinkState): SaveLinkState {
            return oldState.copy(
                originalLink = data.originalLink,
                groupName = data.groupNow,
                timestamp = data.timeStamp,
                extraNote = "",
                recentGroupNameSuggestions = recentGroupNames,
                isSaveButtonsEnabled = true,
                isGroupNameIncorrect = false
            )
        }
    }

    sealed class OnGroupNameUpdateChange : SaveLinkPartialChange() {

        override fun reduce(oldState: SaveLinkState): SaveLinkState {
            return when(this) {
                is InvalidGroupName -> {
                    oldState.copy(
                        isSaveButtonsEnabled = false,
                        isGroupNameIncorrect = true
                    )
                }
                is ValidGroupName -> {
                    oldState.copy(
                        groupName = newGroupName,
                        isSaveButtonsEnabled = true,
                        isGroupNameIncorrect = false
                    )
                }
            }
        }

        object InvalidGroupName : OnGroupNameUpdateChange()
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

}