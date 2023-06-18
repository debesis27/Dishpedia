package com.example.dishpedia.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.dishpedia.R

val Diphylleia = FontFamily(
    Font(R.font.diphylleia_regular)
)

val Courgette = FontFamily(
    Font(R.font.courgette_regular)
)

val Vollkorn = FontFamily(
    Font(R.font.vollkorn_variablefont_weight),
)

val VollkornItalic = FontFamily(
    Font(R.font.vollkorn_italic_variablefont_weight)
)

val Typography = Typography(
    h1 = TextStyle(
        fontFamily = Vollkorn,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
    ),
    h2 = TextStyle(
        fontFamily = Diphylleia,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
    ),
    h3 = TextStyle(
        fontFamily = Diphylleia,
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
    ),
    body1 = TextStyle(
        fontFamily = Courgette,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)