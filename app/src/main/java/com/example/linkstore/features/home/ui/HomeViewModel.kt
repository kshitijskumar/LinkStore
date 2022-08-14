package com.example.linkstore.features.home.ui

import com.example.linkstore.features.home.HomeIntent
import com.example.linkstore.features.home.HomePartialChange
import com.example.linkstore.features.home.HomeSideEffect
import com.example.linkstore.features.home.HomeState
import com.example.linkstore.features.home.domain.GetDistinctGroupNamesUseCase
import com.example.linkstore.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDistinctGroupNamesUseCase: GetDistinctGroupNamesUseCase
) : MviViewModel<HomeState, HomeIntent, HomePartialChange, HomeSideEffect>() {

    override fun getInitialState(): HomeState {
        return HomeState()
    }

    override fun Flow<HomeIntent>.toPartialChange(): Flow<HomePartialChange> {
        return merge(
            handleInitializationIntent(filterIsInstance()),
            handleOnGroupNameClicked(filterIsInstance())
        )
    }

    override fun getSideEffects(change: HomePartialChange): List<HomeSideEffect> {
        val sideEffect: HomeSideEffect? = when(change) {
            is HomePartialChange.GroupNameClickedChange -> {
                HomeSideEffect.NavigateToLinksListScreen(change.clickedGroupName)
            }
            is HomePartialChange.InitializationChange -> {
                null
            }
        }

        return mutableListOf<HomeSideEffect>().apply {
            sideEffect?.let { add(it) }
        }
    }

    private fun handleInitializationIntent(
        flow: Flow<HomeIntent.InitializationIntent>
    ): Flow<HomePartialChange.InitializationChange> {
        return flow.flatMapLatest {
            getDistinctGroupNamesUseCase.invoke()
                .map {
                    HomePartialChange.InitializationChange(it)
                }
        }
    }

    private fun handleOnGroupNameClicked(
        flow: Flow<HomeIntent.OnGroupNameClicked>
    ): Flow<HomePartialChange.GroupNameClickedChange> {
        return flow.map {
            HomePartialChange.GroupNameClickedChange(it.groupName)
        }
    }
}