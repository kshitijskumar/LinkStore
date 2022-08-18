package com.example.linkstore.features.alllinksofgroup.ui

import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.mvi.BasePartialChange

data class GroupLinksState(
    val groupName: String = "",
    val linksList: List<LinkAppModel> = listOf(),
    val shouldShowNoLinksForQuery: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

sealed class GroupLinksIntent {
    data class Initialization(val groupName: String): GroupLinksIntent()
    data class OnLinkClickedToOpen(val link: String): GroupLinksIntent()
    data class OnEditClicked(val linkAppModel: LinkAppModel): GroupLinksIntent()
    data class OnDeleteClicked(val linkAppModel: LinkAppModel): GroupLinksIntent()
    data class OnDeleteConfirmedClicked(val linkAppModel: LinkAppModel): GroupLinksIntent()
    data class OnSearchQueryUpdate(val searchQuery: String): GroupLinksIntent()
}

sealed class GroupLinksSideEffect {
    data class OpenClickedLink(val link: String): GroupLinksSideEffect()
    data class NavigateToEditLinkFlow(val linkAppModel: LinkAppModel): GroupLinksSideEffect()
    data class ShowDeleteConfirmationDialog(val linksAppModel: LinkAppModel): GroupLinksSideEffect()
    object NavigateBack : GroupLinksSideEffect()
}

sealed class GroupLinksPartialChange : BasePartialChange<GroupLinksState> {

    data class InitializationChange(val groupName: String) : GroupLinksPartialChange() {
        override fun reduce(oldState: GroupLinksState): GroupLinksState {
            return oldState.copy(
                groupName = groupName
            )
        }
    }

    data class OnLinkClickedChange(val link: String): GroupLinksPartialChange()

    data class OnEditClickedChange(val linkAppModel: LinkAppModel): GroupLinksPartialChange()

    data class OnDeleteClickedChange(val linkAppModel: LinkAppModel): GroupLinksPartialChange()

    object OnDeleteConfirmedClickedChange: GroupLinksPartialChange()

    sealed class SearchQueryUpdateChange(): GroupLinksPartialChange() {

        override fun reduce(oldState: GroupLinksState): GroupLinksState {
            return when(this) {
                is LinksForTheGroup -> {
                    oldState.copy(
                        shouldShowNoLinksForQuery = false,
                        linksList = linksList,
                        isLoading = false
                    )
                }
                is NoLinksForTheGroup -> {
                    oldState
                }
                NoLinksForTheQuery -> {
                    oldState.copy(
                        shouldShowNoLinksForQuery = true,
                        linksList = listOf(),
                        isLoading = false
                    )
                }
                is OnQueryUpdate -> {
                    oldState.copy(
                        searchQuery = searchQuery
                    )
                }
            }
        }

        data class OnQueryUpdate(val searchQuery: String) : SearchQueryUpdateChange()
        object NoLinksForTheGroup : SearchQueryUpdateChange()
        object NoLinksForTheQuery : SearchQueryUpdateChange()
        data class LinksForTheGroup(val linksList: List<LinkAppModel>) : SearchQueryUpdateChange()
    }

}