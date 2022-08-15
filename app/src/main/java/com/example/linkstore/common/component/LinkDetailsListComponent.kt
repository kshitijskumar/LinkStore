package com.example.linkstore.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.linkstore.R
import com.example.linkstore.features.savelink.data.models.appmodel.LinkAppModel
import com.example.linkstore.ui.theme.BlueishGrey
import com.example.linkstore.ui.theme.MediumGrey
import com.example.linkstore.ui.theme.SubtitleColor

@Composable
fun LinkDetailsListComponent(
    linkAppModel: LinkAppModel,
    onLinkClicked: (LinkAppModel) -> Unit,
    onEditClicked: (LinkAppModel) -> Unit,
    onDeleteClicked: (LinkAppModel) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .clip(RoundedCornerShape(14.dp))
            .background(BlueishGrey, RoundedCornerShape(14.dp))
            .clickable {
                onLinkClicked.invoke(linkAppModel)
            }
            .padding(18.dp)
    ) {
        if (linkAppModel.thumbnailUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(linkAppModel.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .background(MediumGrey)
                    .clip(CutCornerShape(8.dp)),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (!linkAppModel.extraNote.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = linkAppModel.extraNote,
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = linkAppModel.originalLink,
                color = SubtitleColor,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = { onEditClicked.invoke(linkAppModel) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "edit",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                IconButton(
                    onClick = { onDeleteClicked.invoke(linkAppModel) }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_delete),
                        contentDescription = "delete",
                        tint = Color.Black
                    )
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun LinkDetailsListComponentPreview() {
    LinkDetailsListComponent(
        linkAppModel = LinkAppModel(
            originalLink = "something something",
            groupName = "Youtube",
            123456,
            "this is that something link",
            "thumbnial"
        ),
        onLinkClicked = {},
        onEditClicked = {},
        onDeleteClicked = {}
    )
}