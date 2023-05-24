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
import java.lang.Math.abs

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
    val listState = rememberLazyListState(Int.MAX_VALUE / 2)

    BoxWithConstraints(modifier = parentModifier) {
        val halfColumnHeight = constraints.maxHeight / 2

        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(-contentWidth / 2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(
                count = Int.MAX_VALUE,
                itemContent = { globalIndex ->
                    val scale = remember(globalIndex) {
                        val currentItem =
                            listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == globalIndex }
                                ?: return@remember 0.85f

                        (1f - minOf(
                            1f,
                            abs(currentItem.offset + (currentItem.size / 2) - halfColumnHeight).toFloat() / halfColumnHeight ) * 0.25f)
                    }

                    content(
                        index = globalIndex % count,
                        modifier = Modifier
                            .width(contentWidth)
                            .height(contentHeight)
                            .scale(scale)
                            .zIndex(scale * 10)
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
        count = 5,
        parentModifier = Modifier.fillMaxWidth().height(200.dp),
        contentWidth = 150.dp,
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
