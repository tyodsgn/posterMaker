package com.tyodsgn.postermaker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.AddToHomeScreen
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.rounded.NoteAdd
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
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
import java.text.NumberFormat
import java.util.Locale


@Preview(showBackground = true)
@Composable
fun PreviewProductDetail(){
    ProductDetailScreen(modiferTopAppBar= Modifier, navController= rememberNavController())
}

@Composable
fun ProductDetailScreen(modiferTopAppBar: Modifier, navController: NavHostController) {
    var qty by remember { mutableStateOf(1) }
    val initDiscount = 5
    var additionalDiscount by remember { mutableStateOf(initDiscount) }
    var currentDiscount = initDiscount + additionalDiscount
    val productPrice = 120000.0
    val priceWithInitDiscountOnly = productPrice - (initDiscount * productPrice / 100)
    val currentPriceAfterDiscount: Double = productPrice - (currentDiscount * productPrice / 100)
    val totalAmount : Double = currentPriceAfterDiscount * qty
    val totalAmountBeforeDiscount: Double = productPrice * qty

    Scaffold(
        bottomBar = {
            Box(
            ) {
                AddToCartBarView(
                    currentQty = qty,
                    onQtyChange = { qty = it },
                    totalAmount = totalAmount,
                    originalAmount = totalAmountBeforeDiscount,
                    initDiscount = initDiscount,
                    additionalDiscount = additionalDiscount
                )
            }
        }
    ) { innerPadding ->
        val pad = innerPadding
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ProductImageView()
            ProductInfoView(
                navController = navController,
                currentQty = qty,
                finalPrice = priceWithInitDiscountOnly,
                originalPrice = productPrice,
                discount = initDiscount) {
                additionalDiscount = it
            }
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
            .height(270.dp)
    )
}


@Composable
fun discountTag(discount: String = "5%"){
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(4.dp)
            )
            .background(color = Color.Red)
            .padding(horizontal = 3.dp)

    ) {
        Text(
            discount,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight(800),
            color = Color.White
        )
    }
}

@Composable
fun ProductInfoView(
    navController: NavHostController,
    currentQty: Int,
    finalPrice: Double,
    originalPrice: Double,
    discount: Int = 0,
    additionalDiscount: (Int) -> Unit){
    val scrollState = rememberScrollState()
    val boronganGradient = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)))
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        //Main Info
        Column(
            verticalArrangement = Arrangement.spacedBy((-18).dp)
        ) {
            DiscountBadgeLargeView()

            Column(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)

            ) {
                Text(
                    formattedAmount(finalPrice),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.W600
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        formattedAmount(originalPrice),
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )

                    discountTag("$discount%")
                }

                Text(
                    "NK 7328 Sumo - Benih Jagung Hibrida",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.typography.bodyMedium.color
                )

            }
        }

        //Diskon Borongan

        Column(

            modifier = Modifier
                .background(
                    brush = boronganGradient
                )
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text("Diskon Borongan",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W600,
                modifier = Modifier
                    .padding(start = 16.dp)
            )

            DiscountListRow(currentQty = currentQty){
                additionalDiscount(it)
            }
        }

        //Detail Info
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text("Detail Product",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W600)

            Row {
                Text("Minimum Order",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f))

                Text("2 pcs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.typography.bodyMedium.color,
                    modifier = Modifier.weight(1f))
            }
            Row {
                Text("Active Ingredient",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f))

                Text("Glisophat, Heba",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.typography.bodyMedium.color,
                    modifier = Modifier.weight(1f))
            }

        }

        //Desc Info
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            Text("Description",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.W600)

            Text("Nisl purus mollis vestibulum egestas sit vulputate mi pharetra. Bibendum consequat aliquam felis eu ultrices purus vestibulum. Metus sit vestibulum eget lacus pellentesque elementum iaculis. Metus sit vestibulum eget lacus pellentesque elementum iaculis.Metus sit vestibulum eget lacus pellentesque elementum iaculis.Metus sit vestibulum eget lacus pellentesque elementum iaculis. ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))

            Box(
                Modifier.size(60.dp)
            )

        }
    }
}

@Composable
fun PromoteProductView(navController: NavHostController){
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddToCartBarView(
    currentQty: Int,
    totalAmount: Double = 0.0,
    originalAmount: Double = 0.0,
    onQtyChange: (Int) -> Unit,
    initDiscount: Int = 0,
    additionalDiscount: Int = 0
) {
    val discountTag = "$initDiscount%${if (additionalDiscount != 0) " + $additionalDiscount%" else ""}"
    val priceDifferent = originalAmount - totalAmount
    val linearGradientDiscount = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)))
    Column(
        modifier = Modifier
            .background(color = if (additionalDiscount == 0) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.primary)
            .padding(top = 1.dp)

    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
        ) {

            if (additionalDiscount != 0) {
                Box(
                    modifier = Modifier
                        .background(brush = linearGradientDiscount)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text("You have saved in total ${formatCompactCurrency(priceDifferent)} 🎉",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground)
                }
            }

            // Total Amount
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                ) {
                    Text(
                        text = formattedAmount(totalAmount),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Original Price without any discount
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = formattedAmount(originalAmount),
                            style = MaterialTheme.typography.bodyMedium,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )

                        discountTag("$discountTag")
                    }
                }
                
                Box(
                        Modifier.weight(0.5f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Minus
                    IconButton(
                        onClick = {
                            if (currentQty > 1) {
                                onQtyChange(currentQty - 1)
                            }

                        },
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = if (currentQty < 2) 0.4f else 1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .size(scaledDp(44.dp))
                            .alpha(if (currentQty < 2) 0.5f else 1f)
                        ,
                    ) {
                        Icon(
                            Icons.Rounded.Remove,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .width(scaledDp(56.dp))
                            .height(scaledDp(44.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = currentQty.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center)
                    }

                    // Add
                    IconButton(
                        onClick = {
                            onQtyChange(currentQty + 1)
                        },
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .size(scaledDp(44.dp))
                    ) {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }

                }


            }

            // Add to Cart
            Button(
                onClick = {
                    onQtyChange(currentQty + 1)
                    println("Add $currentQty items to cart")
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)

            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                    "Add to Cart",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                ) }
            } }
    }
}

@Composable
fun DiscountBadgeLargeView(){
    Box {
        Image(
            painter = painterResource(id = R.drawable.bg_flash_only),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
        )

        Box(
            modifier = Modifier
                .padding(top = 6.dp, start = 16.dp, end = 6.dp)

        ) {
            Image(
                painter = painterResource(id = R.drawable.flash_sale_large_logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(16.dp)

            )
        }

    }
}

@Composable
fun DiscountListRow(
    currentQty: Int,
    additionalDiscount: (Int) -> Unit
    ) {
    val listState = rememberLazyListState()

    // Find the selected discount index based on current quantity
    val selectedDiscountIndex = sampleDiscounts.indexOfFirst { discount ->
        currentQty in discount.minQty..discount.maxQty
    }.takeIf { it >= 0 } // Handle case when no discount matches

    // Scroll to the selected discount when it changes
    LaunchedEffect(selectedDiscountIndex) {
        selectedDiscountIndex?.let { index ->
            listState.animateScrollToItem(index)
        }
        if (selectedDiscountIndex == null) {
            additionalDiscount(0)
        } else {
            additionalDiscount(sampleDiscounts[selectedDiscountIndex].discount)
        }
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        state = listState
    ) {
        itemsIndexed(sampleDiscounts) { index, discount ->
            val isSelected = index == selectedDiscountIndex

            DiscountCardComponent(
                modifier = Modifier,
                title = discount.title,
                description = discount.description,
                expiryDate = discount.expiryDate,
                strokeColor = MaterialTheme.colorScheme.primary,
                cardWidth = 260.dp,
                isSelected = isSelected
            )
        }
    }
}

fun formattedAmount(totalAmount: Double): String {
    val formattedAmount = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0 // No decimals
    }.format(totalAmount)

    return  formattedAmount
}

fun formatCompactCurrency(amount: Double): String {
    val suffix: String
    val value: Double

    when {
        amount >= 1_000_000_000 -> {
            value = amount / 1_000_000_000
            suffix = "m"
        }
        amount >= 1_000_000 -> {
            value = amount / 1_000_000
            suffix = "jt"
        }
        amount >= 1_000 -> {
            value = amount / 1_000
            suffix = "rb"
        }
        else -> {
            value = amount
            suffix = ""
        }
    }

    val formatted = if (value % 1 == 0.0) value.toInt().toString() else String.format("%.1f", value)

    return "Rp$formatted$suffix"
}


data class DiscountModel(
    val title: String,
    val description: String,
    val expiryDate: String,
    val minQty: Int,
    val maxQty: Int,
    val discount: Int = 0
)

data class ProductModel(
    val name: String,
    val description: String,
    val price: Double,
    val discount: Int = 0,
)

val sampleDiscounts = listOf(
    DiscountModel(
        title = "Buy 3–5 items get 5% discount per item",
        description = "Rp110.000/item",
        expiryDate = "Valid till May 2025",
        minQty = 3,
        maxQty = 5,
        discount = 5
    ),
    DiscountModel(
        title = "Buy 6–10 items get 10% discount per item",
        description = "Rp100.000/item",
        expiryDate = "Valid till May 2025",
        minQty = 6,
        maxQty = 10,
        discount = 10
    ),
    DiscountModel(
        title = "Buy 11+ items get 15% discount per item",
        description = "Rp90.000/item",
        expiryDate = "Valid till May 2025",
        minQty = 11,
        maxQty = Int.MAX_VALUE,
        discount = 15
    )
)
