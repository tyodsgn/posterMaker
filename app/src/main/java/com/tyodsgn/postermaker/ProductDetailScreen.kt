package com.tyodsgn.postermaker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tyodsgn.postermaker.ui.theme.fontHeadingFlex


@Preview(showBackground = true)
@Composable
fun PreviewProductDetail(){
    ProductDetailScreen(modiferTopAppBar= Modifier, navController= rememberNavController())
}

@Composable
fun ProductDetailScreen(modiferTopAppBar: Modifier, navController: NavHostController) {
    Scaffold(
        bottomBar = {
            Box(
                Modifier
                    .padding(16.dp)
            ) {
                AddToCartBarView()
            }
        }
    ) { innerPadding ->
        val pad = innerPadding
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ProductImageView()
            ProductInfoView(navController = navController)
        }
    }
}

@Composable
fun ProductImageView(){
    Image(
        painter = painterResource(id = R.drawable.product_quatis),
        contentDescription = null,
        contentScale = ContentScale.FillHeight,
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            )
            .background(
                color = MaterialTheme.colorScheme.surface
            )
            .fillMaxWidth()
            .height(340.dp)
    )
}

@Composable
fun ProductInfoView(navController: NavHostController){
    Column(
        modifier = Modifier
            .padding(
                horizontal = 2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        //Main Info
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(16.dp)
                .fillMaxWidth()

        ) {
            Text("Rp120.000",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.W600
            )
            Text("Rp120.000",
                style = MaterialTheme.typography.bodyMedium,
                textDecoration = TextDecoration.LineThrough,
                color = MaterialTheme.typography.bodyMedium.color.copy(alpha = 0.5f))

            Text("NK 7328 Sumo - Benih Jagung Hibrida",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.typography.bodyMedium.color)

            Row(
                Modifier
                    .padding(top = 12.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        val id = "bg"
                        navController.navigate("imageEditorScreen/${id}")
                    }
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Promote this product",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.typography.bodyMedium.color,
                    modifier = Modifier.weight(1f))
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        //Detail Info
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text("Detail Product",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.W600)

            Row {
                Text("Minimum Order",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.typography.bodyMedium.color.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f))

                Text("2 pcs",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.typography.bodyMedium.color,
                    modifier = Modifier.weight(1f))
            }
            Row {
                Text("Active Ingredient",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.typography.bodyMedium.color.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f))

                Text("Glisophat, Heba",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.typography.bodyMedium.color,
                    modifier = Modifier.weight(1f))
            }

        }

        //Desc Info
        Column(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text("Description",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.W600)

            Text("Nisl purus mollis vestibulum egestas sit vulputate mi pharetra. Bibendum consequat aliquam felis eu ultrices purus vestibulum. Metus sit vestibulum eget lacus pellentesque elementum iaculis. ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.typography.bodyMedium.color.copy(alpha = 0.5f),
                modifier = Modifier.weight(1f))

        }
    }
}

@Composable
fun AddToCartBarView(){
    Row {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Text("Add to Cart",
                style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = false)
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


//@Preview(showBackground = true)
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
            .width(screenWidth)
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

