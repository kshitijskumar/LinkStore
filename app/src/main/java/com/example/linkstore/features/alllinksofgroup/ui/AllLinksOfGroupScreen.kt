package com.example.linkstore.features.alllinksofgroup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.linkstore.common.component.LinkDetailsListComponent
import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
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

    LaunchedEffect(key1 = Unit) {
        groupVm.processIntent(
            GroupLinksIntent.Initialization(groupName = groupName)
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

                        }
                    }
                }
        }
    }

    val state = groupVm.state.collectAsState()

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
        Spacer(modifier = Modifier.height(18.dp))
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