package com.example.linkstore.features.main

import com.example.linkstore.common.utils.ResultData
import com.example.linkstore.features.linkprocessor.LinkProcessor
import com.example.linkstore.features.main.MainActivityNavigationPartialChange.SaveLinkOrHomeScreenPartialChange.*
import com.example.linkstore.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

@HiltViewModel
class MainActivityNavigationViewModel @Inject constructor(
    private val linkProcessor: LinkProcessor
) : MviViewModel<MainActivityState, MainActivityNavigationIntent, MainActivityNavigationPartialChange, MainActivityNavigationSideEffect>() {

    override fun getInitialState() = MainActivityState()

    override fun Flow<MainActivityNavigationIntent>.toPartialChange(): Flow<MainActivityNavigationPartialChange> {
        return merge(
            handleTryNavigateToSaveLinkOrHomeScreenIntent(filterIsInstance())
        )
    }

    override fun getSideEffects(change: MainActivityNavigationPartialChange): List<MainActivityNavigationSideEffect> {
        val sideEffect: MainActivityNavigationSideEffect? = when (change) {
            is HomeScreenFlow -> {
                MainActivityNavigationSideEffect.NavigateToHomeScreenSideEffect
            }
            is SaveLinkFlow -> {
                MainActivityNavigationSideEffect.NavigateToSaveLinkScreenSideEffect(change.processedLinkData)
            }
        }

        return mutableListOf<MainActivityNavigationSideEffect>().apply {
            sideEffect?.let { add(it) }
        }
    }

    private fun handleTryNavigateToSaveLinkOrHomeScreenIntent(
        flow: Flow<MainActivityNavigationIntent.TryNavigateToSaveLinkOrHomeScreenIntent>
    ): Flow<MainActivityNavigationPartialChange.SaveLinkOrHomeScreenPartialChange> {
        return flow.map {
            when (val result = linkProcessor.processLink(it.originalLink)) {
                is ResultData.Error -> HomeScreenFlow
                is ResultData.Success -> SaveLinkFlow(result.data)
            }
        }
    }

}