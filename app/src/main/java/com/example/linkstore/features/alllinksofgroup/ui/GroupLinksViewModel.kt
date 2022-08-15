package com.example.linkstore.features.alllinksofgroup.ui

import com.example.linkstore.features.alllinksofgroup.domain.DeleteLinkUseCase
import com.example.linkstore.features.alllinksofgroup.domain.GetAllLinksForGroupNameUseCase
import com.example.linkstore.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GroupLinksViewModel @Inject constructor(
    private val getAllLinksForGroupNameUseCase: GetAllLinksForGroupNameUseCase,
    private val deleteLinkUseCase: DeleteLinkUseCase
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
            handleOnDeleteConfirmedClickedIntent(filterIsInstance())
        )
    }

    override fun getSideEffects(change: GroupLinksPartialChange): List<GroupLinksSideEffect> {
        val sideEffect: GroupLinksSideEffect? = when(change) {
            is GroupLinksPartialChange.InitializationChange -> null
            is GroupLinksPartialChange.OnDeleteClickedChange -> {
                GroupLinksSideEffect.ShowDeleteConfirmationDialog(change.linkAppModel)
            }
            is GroupLinksPartialChange.OnDeleteConfirmedClickedChange -> {
                GroupLinksSideEffect.NavigateBack
            }
            is GroupLinksPartialChange.OnEditClickedChange -> {
                GroupLinksSideEffect.NavigateToEditLinkFlow(change.linkAppModel)
            }
            is GroupLinksPartialChange.OnLinkClickedChange -> {
                GroupLinksSideEffect.OpenClickedLink(change.link)
            }
        }

        return mutableListOf<GroupLinksSideEffect>().apply {
            sideEffect?.let { add(it) }
        }
    }

    private fun handleInitializationIntent(
        flow: Flow<GroupLinksIntent.Initialization>
    ): Flow<GroupLinksPartialChange.InitializationChange> {
        return flow.flatMapLatest { intent ->
            getAllLinksForGroupNameUseCase.invoke(intent.groupName)
                .map { list ->
                    GroupLinksPartialChange.InitializationChange(
                        groupName = intent.groupName,
                        linksList = list
                    )
                }
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
}