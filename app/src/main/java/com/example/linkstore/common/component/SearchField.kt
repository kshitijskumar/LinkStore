package com.example.linkstore.common.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.linkstore.ui.theme.LightGrey
import com.example.linkstore.ui.theme.MediumGrey
import com.example.linkstore.ui.theme.SubtitleColor

@Composable
fun SearchField(
    initialText: String,
    placeholderText: String,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {

    val text = remember {
        mutableStateOf(initialText)
    }

    TextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onValueChanged.invoke(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .padding(bottom = 2.dp),
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            backgroundColor = LightGrey,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = SubtitleColor
        ),
        singleLine = true,
        placeholder = {
            Text(
                text = placeholderText,
                color = MediumGrey
            )
        }
    )

}