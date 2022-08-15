package com.example.linkstore.features.main

import com.example.linkstore.features.linkprocessor.ProcessedLinkData
import com.example.linkstore.mvi.BasePartialChange

class MainActivityState

sealed class MainActivityNavigationIntent {
    data class TryNavigateToSaveLinkOrHomeScreenIntent(val originalLink: String?) : MainActivityNavigationIntent()
    data class NavigateToSaveLinkIntent(val originalLink: String?) : MainActivityNavigationIntent()
    data class NavigateToAllLinksOfGroupIntent(val groupName: String): MainActivityNavigationIntent()
}

sealed class MainActivityNavigationSideEffect {
    data class NavigateToSaveLinkScreenSideEffect(val processedLinkData: ProcessedLinkData) : MainActivityNavigationSideEffect()
    object NavigateToHomeScreenSideEffect : MainActivityNavigationSideEffect()
    data class NavigateToAllLinksOfGroupSideEffect(val groupName: String): MainActivityNavigationSideEffect()
}

sealed class MainActivityNavigationPartialChange : BasePartialChange<MainActivityState> {
    sealed class SaveLinkOrHomeScreenPartialChange : MainActivityNavigationPartialChange() {

        override fun reduce(oldState: MainActivityState): MainActivityState {
            return when(this) {
                is HomeScreenFlow -> oldState
                is SaveLinkFlow -> oldState
            }
        }

        data class SaveLinkFlow(val processedLinkData: ProcessedLinkData) : SaveLinkOrHomeScreenPartialChange()
        object HomeScreenFlow : SaveLinkOrHomeScreenPartialChange()
    }

    data class AllLinksOfGroupPartialChange(val groupName: String) : MainActivityNavigationPartialChange()
}