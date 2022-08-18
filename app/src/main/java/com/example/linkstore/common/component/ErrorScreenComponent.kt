package com.example.linkstore.common.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkstore.R
import com.example.linkstore.ui.theme.SubtitleColor

@Composable
fun ErrorScreenComponent(
    errorTitle: String,
    errorSubTitle: String
) {
    Column {
        Text(
            text = errorTitle,
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
            text = errorSubTitle,
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