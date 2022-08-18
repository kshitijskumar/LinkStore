package com.example.linkstore.features.alllinksofgroup.ui

import com.example.linkstore.features.alllinksofgroup.domain.DeleteLinkUseCase
import com.example.linkstore.features.alllinksofgroup.domain.GetSearchQueryRelatedLinksUseCase
import com.example.linkstore.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GroupLinksViewModel @Inject constructor(
    private val deleteLinkUseCase: DeleteLinkUseCase,
    private val getSearchQueryRelatedLinksUseCase: GetSearchQueryRelatedLinksUseCase
) : MviViewModel<GroupLinksState, GroupLinksIntent, GroupLinksPartialChange, GroupLinksSideEffect>() {

    override fun getInitialState(): GroupLinksState {
        return GroupLinksState()
    }

    override fun Flow<GroupLinksIntent>.toPartialChange(): Flow<GroupLinksPartialChange> {
        return merge(
            handleInitializationIntent(filterIsInstance()),
            handleOnLinkClickedToOpenIntent(filterIsInstance()),
            handleOnEditClickedIntent(filterIsInstance()),
            handleOnDeleteClickedIntent(filterIsInstance()),
            handleOnDeleteConfirmedClickedIntent(filterIsInstance()),
            handleSearchQueryUpdateIntent(filterIsInstance())
        )
    }

    override fun getSideEffects(change: GroupLinksPartialChange): List<GroupLinksSideEffect> {
        val sideEffect: GroupLinksSideEffect? = when(change) {
            is GroupLinksPartialChange.OnDeleteClickedChange -> {
                GroupLinksSideEffect.ShowDeleteConfirmationDialog(change.linkAppModel)
            }
            is GroupLinksPartialChange.OnEditClickedChange -> {
                GroupLinksSideEffect.NavigateToEditLinkFlow(change.linkAppModel)
            }
            is GroupLinksPartialChange.OnLinkClickedChange -> {
                GroupLinksSideEffect.OpenClickedLink(change.link)
            }
            is GroupLinksPartialChange.InitializationChange -> {
                null
            }
            is GroupLinksPartialChange.OnDeleteConfirmedClickedChange -> null
            is GroupLinksPartialChange.SearchQueryUpdateChange.LinksForTheGroup -> null
            GroupLinksPartialChange.SearchQueryUpdateChange.NoLinksForTheGroup -> {
                GroupLinksSideEffect.NavigateBack
            }
            GroupLinksPartialChange.SearchQueryUpdateChange.NoLinksForTheQuery -> null
            is GroupLinksPartialChange.SearchQueryUpdateChange.OnQueryUpdate -> null
        }

        return mutableListOf<GroupLinksSideEffect>().apply {
            sideEffect?.let { add(it) }
        }
    }

    private fun handleInitializationIntent(
        flow: Flow<GroupLinksIntent.Initialization>
    ): Flow<GroupLinksPartialChange> {
        return flow.map {
            GroupLinksPartialChange.InitializationChange(it.groupName)
        }
    }

    private fun handleOnLinkClickedToOpenIntent(
        flow: Flow<GroupLinksIntent.OnLinkClickedToOpen>
    ): Flow<GroupLinksPartialChange.OnLinkClickedChange> {
        return flow.map {
            GroupLinksPartialChange.OnLinkClickedChange(it.link)
        }
    }

    private fun handleOnEditClickedIntent(
        flow: Flow<GroupLinksIntent.OnEditClicked>
    ): Flow<GroupLinksPartialChange.OnEditClickedChange> {
        return flow.map {
            GroupLinksPartialChange.OnEditClickedChange(it.linkAppModel)
        }
    }

    private fun handleOnDeleteClickedIntent(
        flow: Flow<GroupLinksIntent.OnDeleteClicked>
    ): Flow<GroupLinksPartialChange.OnDeleteClickedChange> {
        return flow.map {
            GroupLinksPartialChange.OnDeleteClickedChange(it.linkAppModel)
        }
    }

    private fun handleOnDeleteConfirmedClickedIntent(
        flow: Flow<GroupLinksIntent.OnDeleteConfirmedClicked>
    ): Flow<GroupLinksPartialChange.OnDeleteConfirmedClickedChange> {
        return flow.map {
            deleteLinkUseCase.invoke(it.linkAppModel.originalLink)
            GroupLinksPartialChange.OnDeleteConfirmedClickedChange
        }
    }

    private fun handleSearchQueryUpdateIntent(
        flow: Flow<GroupLinksIntent.OnSearchQueryUpdate>
    ): Flow<GroupLinksPartialChange.SearchQueryUpdateChange> {
        return flow.flatMapLatest {
            val queryResultFlow = flow {
                delay(200)
                emit(it.searchQuery)
            }.flatMapLatest {
                getSearchQueryRelatedLinksUseCase.invoke(
                    groupName = state.value.groupName,
                    searchQuery = it
                ).map { list ->
                    if (list.isNotEmpty()) {
                        GroupLinksPartialChange.SearchQueryUpdateChange.LinksForTheGroup(list)
                    } else {
                        if (it.isEmpty()) {
                            GroupLinksPartialChange.SearchQueryUpdateChange.NoLinksForTheGroup
                        } else {
                            GroupLinksPartialChange.SearchQueryUpdateChange.NoLinksForTheQuery
                        }
                    }
                }
            }

            merge(
                flowOf(GroupLinksPartialChange.SearchQueryUpdateChange.OnQueryUpdate(it.searchQuery)),
                queryResultFlow
            )
        }
    }
}