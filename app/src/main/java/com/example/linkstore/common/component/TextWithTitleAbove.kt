package com.example.linkstore.common.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkstore.ui.theme.LightGrey
import com.example.linkstore.ui.theme.SubtitleColor

@Composable
fun TextWithTitleAbove(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .background(LightGrey, RoundedCornerShape(4.dp))
        .clip(RoundedCornerShape(4.dp))
        .padding(horizontal = 14.dp, vertical = 12.dp)
        .padding(bottom = 2.dp),
    title: String,
    text: String,
    titleTextColor: Color = SubtitleColor,
    textColor: Color = Color.Black
) {

    val uriHandler = LocalUriHandler.current

    val annotatedString = buildAnnotatedString {
        append(text = text)
        addStyle(
            style = SpanStyle(
                color = textColor,
                textDecoration = TextDecoration.Underline
            ),
            start = 0,
            end = text.length
        )
        addStringAnnotation(
            tag = "URL",
            annotation = text,
            start = 0,
            end = text.length
        )
    }
    Column {
        Text(
            text = title,
            color = titleTextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        )
        Text(
            text = annotatedString,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = modifier
                .clickable {
                    annotatedString
                        .getStringAnnotations("URL", 0, annotatedString.length)
                        .firstOrNull()?.let {
                            uriHandler.openUri(it.item)
                        }
                },
            textAlign = TextAlign.Start
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun TextWithTitleAbovePreview() {
    TextWithTitleAbove(
        title = "component title",
        text = "component text",
        titleTextColor = SubtitleColor,
        textColor = Color.Black
    )
}