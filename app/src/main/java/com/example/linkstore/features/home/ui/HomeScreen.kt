package com.example.linkstore.features.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.linkstore.R
import com.example.linkstore.common.component.GroupNameComponent
import com.example.linkstore.features.home.HomeIntent
import com.example.linkstore.features.home.HomeSideEffect
import com.example.linkstore.features.home.data.models.GroupNameAppModel
import com.example.linkstore.ui.theme.SubtitleColor
import com.example.linkstore.ui.theme.TitleColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@Composable
fun HomeScreen(
    homeVm: HomeViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        homeVm.processIntent(HomeIntent.InitializationIntent)
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            homeVm.sideEffect
                .collect {
                    when (it) {
                        is HomeSideEffect.NavigateToLinksListScreen -> {

                        }
                    }
                }
        }
    }

    val state = homeVm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(id = R.string.link_groups),
            fontSize = 28.sp,
            fontWeight = FontWeight.Medium,
            color = TitleColor,
            modifier = Modifier.padding(horizontal = 18.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.value.groupNamesList.isNotEmpty()) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 4.dp),
                ) {
                    items(items = state.value.groupNamesList) { item: GroupNameAppModel ->
                        GroupNameComponent(
                            groupNameAppModel = item,
                            onGroupClicked =  {
                                scope.launch {
                                    homeVm.processIntent(
                                        HomeIntent.OnGroupNameClicked(it.groupName)
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            } else if (state.value.shouldShowErrorIfListEmpty) {
                Column {
                    Text(
                        text = stringResource(id = R.string.no_group_error_title),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = SubtitleColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.np_group_error_subtitle),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = SubtitleColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}