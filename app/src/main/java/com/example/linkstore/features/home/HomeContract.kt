package com.example.linkstore.features.home

import com.example.linkstore.features.home.data.models.GroupNameAppModel
import com.example.linkstore.mvi.BasePartialChange

data class HomeState(
    val groupNamesList: List<GroupNameAppModel> = listOf(),
    val shouldShowErrorIfListEmpty: Boolean = false
)

sealed class HomeIntent {
    object InitializationIntent : HomeIntent()
    data class OnGroupNameClicked(val groupName: String): HomeIntent()
}

sealed class HomeSideEffect {
    data class NavigateToLinksListScreen(val groupName: String): HomeSideEffect()
}

sealed class HomePartialChange : BasePartialChange<HomeState> {

    data class InitializationChange(val groupNamesList: List<GroupNameAppModel>) : HomePartialChange() {
        override fun reduce(oldState: HomeState): HomeState {
            return oldState.copy(
                groupNamesList = groupNamesList,
                shouldShowErrorIfListEmpty = groupNamesList.isEmpty()
            )
        }
    }

    data class GroupNameClickedChange(val clickedGroupName: String): HomePartialChange()

}