package com.example.linkstore.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.linkstore.R
import com.example.linkstore.features.home.data.models.GroupNameAppModel
import com.example.linkstore.ui.theme.BlueishGrey
import com.example.linkstore.ui.theme.Purple200
import com.example.linkstore.ui.theme.SubtitleColor

@Composable
fun GroupNameComponent(
    groupNameAppModel: GroupNameAppModel,
    onGroupClicked: (GroupNameAppModel) -> Unit
) {

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(BlueishGrey, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable { onGroupClicked.invoke(groupNameAppModel) }
            .padding(horizontal = 14.dp, vertical = 14.dp),
    ) {
        Text(
            text = groupNameAppModel.groupName,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.x_number_of_links, groupNameAppModel.countOfLinksInGroup),
            color = SubtitleColor,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(2.dp))
    }

}

@Preview(showSystemUi = true)
@Composable
fun GroupNameComponentPreview() {
    GroupNameComponent(
        groupNameAppModel = GroupNameAppModel(groupName = "Youtube", countOfLinksInGroup = 3),
        onGroupClicked = {

        }
    )
}