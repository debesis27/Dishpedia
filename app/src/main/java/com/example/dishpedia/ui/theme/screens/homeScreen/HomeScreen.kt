package com.example.dishpedia.ui.theme.screens.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

            }
        }
    )
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}
