package com.example.linkstore.features.savelink.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.linkstore.R
import com.example.linkstore.common.component.TextFieldWithTitleAbove
import com.example.linkstore.common.component.TextWithTitleAbove
import com.example.linkstore.features.linkprocessor.ProcessedLinkData
import com.example.linkstore.features.savelink.SaveLinkIntent
import com.example.linkstore.features.savelink.SaveLinkSideEffect
import com.example.linkstore.ui.theme.SubtitleColor
import com.example.linkstore.ui.theme.TitleColor
import kotlinx.coroutines.launch

@Composable
fun SaveLinkScreen(
    processedLinkData: ProcessedLinkData?,
    saveLinkVm: SaveLinkViewModel = hiltViewModel(),
    navigateToHomeScreen: () -> Unit,
    closeApp: () -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        processedLinkData?.let {
            saveLinkVm.processIntent(SaveLinkIntent.InitializationIntent(processedLinkData))
        }
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            saveLinkVm.sideEffect
                .collect {
                    when(it) {
                        SaveLinkSideEffect.ExitApp -> closeApp.invoke()
                        SaveLinkSideEffect.GotoHomeScreen -> navigateToHomeScreen.invoke()
                    }
                }
        }
    }

    val state = saveLinkVm.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .padding(top = 14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.save_link),
                modifier = Modifier,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = TitleColor
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(18.dp)
            )
            Text(
                text = stringResource(id = R.string.save_link_subtitle),
                modifier = Modifier,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = SubtitleColor
            )
            Spacer(modifier = Modifier.height(52.dp))
            TextWithTitleAbove(
                title = stringResource(id = R.string.link),
                text = state.value.originalLink
            )
            Spacer(modifier = Modifier.height(14.dp))
            TextFieldWithTitleAbove(
                title = stringResource(id = R.string.group_name),
                text = state.value.groupName
            ) {
                scope.launch {
                    saveLinkVm.processIntent(
                        SaveLinkIntent.OnGroupNameUpdate(
                            newGroupName = it
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            TextFieldWithTitleAbove(
                title = stringResource(id = R.string.extra_note),
                text = state.value.extraNote ?: ""
            ) {
                scope.launch {
                    saveLinkVm.processIntent(
                        SaveLinkIntent.OnExtraNoteUpdate(
                            newExtraNote = it
                        )
                    )
                }
            }
        }

        SaveButtonRow(
            onPositiveButtonClicked = {
                scope.launch {
                    saveLinkVm.processIntent(
                        SaveLinkIntent.SaveAndGotoHomeClicked
                    )
                }
            },
            onNegativeButtonClicks = {
                scope.launch {
                    saveLinkVm.processIntent(
                        SaveLinkIntent.SaveAndExitClicked
                    )
                }
            }
        )
    }


}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SaveLinkScreenPreview() {
    SaveLinkScreen(
        processedLinkData = ProcessedLinkData("dfs", "Sdf", 123),
        navigateToHomeScreen = {},
        closeApp = {}
    )
}