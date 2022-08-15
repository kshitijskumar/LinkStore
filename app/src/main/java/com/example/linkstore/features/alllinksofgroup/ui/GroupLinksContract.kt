package com.example.linkstore.features.alllinksofgroup.ui

import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.mvi.BasePartialChange

data class GroupLinksState(
    val groupName: String = "",
    val linksList: List<LinkAppModel> = listOf()
)

sealed class GroupLinksIntent {
    data class Initialization(val groupName: String): GroupLinksIntent()
    data class OnLinkClickedToOpen(val link: String): GroupLinksIntent()
    data class OnEditClicked(val linkAppModel: LinkAppModel): GroupLinksIntent()
    data class OnDeleteClicked(val linkAppModel: LinkAppModel): GroupLinksIntent()
    data class OnDeleteConfirmedClicked(val linkAppModel: LinkAppModel): GroupLinksIntent()
}

sealed class GroupLinksSideEffect {
    data class OpenClickedLink(val link: String): GroupLinksSideEffect()
    data class NavigateToEditLinkFlow(val linkAppModel: LinkAppModel): GroupLinksSideEffect()
    data class ShowDeleteConfirmationDialog(val linksAppModel: LinkAppModel): GroupLinksSideEffect()
    object NavigateBack : GroupLinksSideEffect()
}

sealed class GroupLinksPartialChange : BasePartialChange<GroupLinksState> {

    data class InitializationChange(val groupName: String, val linksList: List<LinkAppModel>) : GroupLinksPartialChange() {
        override fun reduce(oldState: GroupLinksState): GroupLinksState {
            return oldState.copy(
                groupName = groupName,
                linksList = linksList
            )
        }
    }

    data class OnLinkClickedChange(val link: String): GroupLinksPartialChange()

    data class OnEditClickedChange(val linkAppModel: LinkAppModel): GroupLinksPartialChange()

    data class OnDeleteClickedChange(val linkAppModel: LinkAppModel): GroupLinksPartialChange()

    object OnDeleteConfirmedClickedChange: GroupLinksPartialChange()

}