package com.tyodsgn.postermaker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyodsgn.postermaker.ui.theme.fontHeadingFlex

@Composable
fun StickerProductInfoView(
    name: String = "NK 7328 Sumo - Benih Jagung Hibrida",
    price: String = "50000",
    discount: String = "5000",
    modifier: Modifier = Modifier,
){

    Box (
        modifier
            .clip(RoundedCornerShape(36.dp))
            .background(color = Color.Black)
            .padding(4.dp)
    ){
        Column(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .width(260.dp)
                .clip(
                    RoundedCornerShape(32.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                )


        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                ProductImageView()
            }

            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    name,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    color = MaterialTheme.typography.bodyMedium.color,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                )
                Text(
                    discount,
                    style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontFamily = fontHeadingFlex,
                        fontSize = MaterialTheme.typography.headlineMedium.fontSize
                    )
                )
                Text(
                    price,
                    style = TextStyle(
                        fontWeight = FontWeight.W600,
                        fontFamily = fontHeadingFlex,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        color = MaterialTheme.colorScheme.error,
                        textDecoration = TextDecoration.LineThrough
                    )
                )


            }


        }
    }
}



@Composable
fun TextStickerView(
    text: String = "Herbisida Paling Ampuh",
    screenWidth: Dp = 80.dp
){
    val fFamily = fontHeadingFlex
    val fWeight = FontWeight.W600
    val fSize = MaterialTheme.typography.headlineLarge.fontSize
    val fSpacing = 0.1.sp
    val fAlign = TextAlign.Center

    Box(
        Modifier
            .width(screenWidth),
        contentAlignment = Alignment.Center
    ) {

        // Text with shadow
        Text(
            text = text,
            fontWeight = fWeight,
            fontSize = fSize,
            color = Color.Black,
            style = TextStyle(
                drawStyle = Stroke(
                    miter = 5f,
                    width = 8f,
                    join = StrokeJoin.Round
                ),
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(0.0f, 10.0f),
                    blurRadius = 0.0f
                ),
                fontFamily = fFamily
            ),
            letterSpacing = fSpacing,
            textAlign = fAlign
        )

        Text(
            text = text,
            fontWeight = fWeight,
            fontSize = fSize,
            color = Color.White,
            style = TextStyle(

                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(0.0f, 10.0f),
                    blurRadius = 0.0f
                )
            ),
            letterSpacing = fSpacing,
            textAlign = fAlign,
            fontFamily = fFamily
        )
    }
}