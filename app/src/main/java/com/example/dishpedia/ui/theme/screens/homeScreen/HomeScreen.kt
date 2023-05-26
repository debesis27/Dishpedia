package com.example.dishpedia.ui.theme.screens.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.dishpedia.R

@Composable
fun HomeScreen(){
    Scaffold(
        content = {
            Column(modifier = Modifier) {
                Row(
                    modifier = Modifier
                        .padding(top = 14.dp,start = 3.dp, end = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .padding(end = 4.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        text = stringResource(id = R.string.categories),
                        modifier = Modifier
                    )
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .padding(start = 4.dp)
                            .clip(CircleShape)
                    )
                }
                //TODO: Make list and stuffs, then check whether carousel works
//                Carousel(
//                    count = list.size,
//                    contentWidth = maxWidth,
//                    contentHeight = 200.dp,
//                    content = { modifier, index ->
//                        MyComposableContent(
//                            item = list[index],
//                            modifier = modifier
//                        )
//                    }
//                )
            }
        }
    )
}

@Composable
fun Carousel(
    count: Int,
    parentModifier: Modifier = Modifier.fillMaxWidth().height(540.dp),
    contentWidth: Dp,
    contentHeight: Dp,
    content: @Composable (modifier: Modifier, index: Int) -> Unit
) {

    BoxWithConstraints(modifier = parentModifier) {
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                count = count,
                itemContent = { globalIndex ->
                    content(
                        index = globalIndex % count,
                        modifier = Modifier
                            .width(contentWidth)
                            .height(contentHeight)
                            .padding(start = 4.dp, end = 4.dp)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    Carousel(
        count = 10,
        parentModifier = Modifier.fillMaxWidth().height(200.dp),
        contentWidth = 250.dp,
        contentHeight = 200.dp
    ) { modifier, index ->
        Box(
            modifier = modifier
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Item ${index + 1}",
                style = TextStyle(color = Color.White, fontSize = 20.sp)
            )
        }
    }
}
