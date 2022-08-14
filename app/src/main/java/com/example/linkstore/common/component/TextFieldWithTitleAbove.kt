package com.example.linkstore.common.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkstore.ui.theme.LightGrey
import com.example.linkstore.ui.theme.SubtitleColor

@Composable
fun TextFieldWithTitleAbove(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(4.dp))
        .padding(bottom = 2.dp),
    title: String,
    text: String,
    titleTextColor: Color = SubtitleColor,
    textColor: Color = Color.Black,
    onValueChanged: (String) -> Unit
) {

    Column {
        Text(
            text = title,
            color = titleTextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        )
        TextField(
            value = text,
            onValueChange = {
                onValueChanged.invoke(it)
            },
            modifier = modifier,
            shape = RoundedCornerShape(4.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor,
                backgroundColor = LightGrey,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

            ),
            maxLines = 8
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun TextFieldWithTitleAbovePreview() {
    TextFieldWithTitleAbove(
        title = "Group name",
        text = "something something") {

    }
}