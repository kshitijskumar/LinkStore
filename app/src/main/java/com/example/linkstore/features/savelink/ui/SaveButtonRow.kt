package com.example.linkstore.features.savelink.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.linkstore.R
import com.example.linkstore.features.savelink.SaveLinkIntent
import com.example.linkstore.ui.theme.LightPurple
import com.example.linkstore.ui.theme.Pink
import com.example.linkstore.ui.theme.Purple200
import kotlinx.coroutines.launch

@Composable
fun SaveButtonRow(
    onPositiveButtonClicked: () -> Unit,
    onNegativeButtonClicks: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Button(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Pink, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Pink
            ),
            onClick = {
                onPositiveButtonClicked.invoke()
            }
        ) {
            Text(
                text = stringResource(id = R.string.save_and_goto_home),
                modifier = Modifier
                    .padding(vertical = 4.dp),
                color = Color.White
            )
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(LightPurple, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = LightPurple
            ),
            onClick = {
                onNegativeButtonClicks.invoke()
            }
        ) {
            Text(
                text = stringResource(id = R.string.save_and_exit_app),
                modifier = Modifier
                    .padding(vertical = 4.dp),
                color = Color.White
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun SaveButtonRowPreview() {
    SaveButtonRow(
        onPositiveButtonClicked = {},
        onNegativeButtonClicks = {}
    )
}