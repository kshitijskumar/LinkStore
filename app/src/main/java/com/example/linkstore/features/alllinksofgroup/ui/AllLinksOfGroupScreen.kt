package com.example.linkstore.features.alllinksofgroup.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.linkstore.R
import com.example.linkstore.common.component.ErrorScreenComponent
import com.example.linkstore.common.component.LinkDetailsListComponent
import com.example.linkstore.common.component.SearchField
import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.ui.theme.Pink
import com.example.linkstore.ui.theme.SubtitleColor
import com.example.linkstore.ui.theme.TitleColor
import kotlinx.coroutines.launch

@Composable
fun AllLinksOfGroupScreen(
    groupName: String,
    groupVm: GroupLinksViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToEditFlow: (LinkAppModel) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    val state = groupVm.state.collectAsState()
    val linkUnderDeleteConsideration = remember {
        mutableStateOf<LinkAppModel?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        groupVm.processIntent(
            GroupLinksIntent.Initialization(groupName = groupName)
        )
        groupVm.processIntent(
            GroupLinksIntent.OnSearchQueryUpdate("")
        )

        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            groupVm.sideEffect
                .collect {
                    when(it) {
                        is GroupLinksSideEffect.NavigateBack -> {
                            navigateBack.invoke()
                        }
                        is GroupLinksSideEffect.NavigateToEditLinkFlow -> {
                            navigateToEditFlow.invoke(it.linkAppModel)
                        }
                        is GroupLinksSideEffect.OpenClickedLink -> {
                            uriHandler.openUri(it.link)
                        }
                        is GroupLinksSideEffect.ShowDeleteConfirmationDialog -> {
                            linkUnderDeleteConsideration.value = it.linksAppModel
                        }
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Text(
            text = state.value.groupName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = TitleColor,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        SearchField(
            initialText = state.value.searchQuery,
            placeholderText = stringResource(id = R.string.search_here),
            modifier = Modifier.padding(horizontal = 18.dp),
            onValueChanged = {
                scope.launch {
                    groupVm.processIntent(GroupLinksIntent.OnSearchQueryUpdate(it))
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.value.isLoading) {
                CircularProgressIndicator(
                    color = Pink
                )
            }
            if(state.value.shouldShowNoLinksForQuery) {
                ErrorScreenComponent(
                    errorTitle = stringResource(id = R.string.no_result_for_query_error_title),
                    errorSubTitle = stringResource(id = R.string.no_result_for_query_error_subtitle)
                )

            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
            ) {
                items(items = state.value.linksList) { item ->
                    LinkDetailsListComponent(
                        linkAppModel = item,
                        onLinkClicked = {
                            scope.launch {
                                groupVm.processIntent(
                                    GroupLinksIntent.OnLinkClickedToOpen(it.originalLink)
                                )
                            }
                        },
                        onEditClicked = {
                            scope.launch {
                                groupVm.processIntent(
                                    GroupLinksIntent.OnEditClicked(it)
                                )
                            }
                        },
                        onDeleteClicked = {
                            scope.launch {
                                groupVm.processIntent(
                                    GroupLinksIntent.OnDeleteClicked(it)
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }

    if (linkUnderDeleteConsideration.value != null) {
        AlertDialog(
            onDismissRequest = {
                linkUnderDeleteConsideration.value = null
            },
            properties = DialogProperties(),
            title = {
                Text(
                    text = stringResource(id = R.string.delete_confirmation_title),
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.delete_confirmation_text),
                    color = SubtitleColor,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    // yes button
                    Text(
                        text = stringResource(id = R.string.yes),
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    linkUnderDeleteConsideration.value?.let {
                                        groupVm.processIntent(
                                            GroupLinksIntent.OnDeleteConfirmedClicked(
                                                it
                                            )
                                        )
                                    }
                                    linkUnderDeleteConsideration.value = null
                                }
                            }
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    // no button
                    Text(
                        text = stringResource(id = R.string.no),
                        modifier = Modifier
                            .clickable {
                                linkUnderDeleteConsideration.value = null
                            }
                            .padding(4.dp)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                }
            }
        )
    }

}