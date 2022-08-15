package com.example.linkstore.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.linkstore.ui.theme.LightGrey
import com.example.linkstore.ui.theme.SubtitleColor

@Composable
fun ImageWithTitleAbove(
    imageUrl: String,
    title: String,
    titleTextColor: Color = SubtitleColor,
    modifier: Modifier = Modifier.size(width = 200.dp, height = 200.dp)
) {
    Column {
        Text(
            text = title,
            color = titleTextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        )
        Spacer(modifier = Modifier.height(4.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = modifier
        )
    }
}