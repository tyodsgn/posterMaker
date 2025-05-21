package com.tyodsgn.postermaker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Discount
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun DiscountCardComponent(
    modifier: Modifier = Modifier,
    title: String = "Buy 3-5 items get 5% discount",
    description: String = "Rp186.000/item",
    expiryDate: String = "Valid till May 2025",
    strokeColor: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 4f,
    corner: Float = 14f,
    cardWidth: Dp = 272.dp,
    isSelected: Boolean = true,
) {
    Box(
        modifier = modifier.width(cardWidth),
        contentAlignment = Alignment.BottomEnd
    ) {
        // We only need one CardDiscountBackgroundView with conditional properties
        CardDiscountBackgroundView(
            title = title,
            description = description,
            expiryDate = expiryDate,
            strokeColor = if(isSelected) strokeColor else MaterialTheme.colorScheme.background,
            strokeWidth = if(isSelected) strokeWidth else 0f,
            corner = corner
        )

        CardDiscountBackgroundView(
            title = title,
            description = description,
            expiryDate = expiryDate,
            strokeColor = MaterialTheme.colorScheme.background,
            strokeWidth = 0f,
            corner = corner
        )

        // Show "Applied" badge only when the discount is selected
        if (isSelected) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(100))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(scaledDp(12.dp))
                )
                Text(
                    "Applied",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun CardDiscountBackgroundView(
    title: String,
    description: String,
    expiryDate: String,
    strokeColor: Color,
    strokeWidth: Float,
    corner: Float
) {
    val backgroundCard: Color = MaterialTheme.colorScheme.background
    Column(modifier = Modifier) {
        // Top part of the card
        Row(
            modifier = Modifier
                .drawCardStroke(
                    corner = corner,
                    strokeColor = strokeColor,
                    strokeWidth = strokeWidth,
                    isTop = true
                )
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = (corner - 2).dp, topEnd = (corner - 2).dp))
                .background(backgroundCard)
                .padding(top = 8.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Rounded.Discount,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(scaledDp(24.dp)).padding(top = 4.dp)
            )

            Box {
                Column(
                    modifier = Modifier.alpha(0f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "Veryyy loonng text so it become 2 line everywhere thats what we want!",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Middle divider
        CardDiscountDividerView(strokeColor = strokeColor, strokeWidth = strokeWidth)

        // Bottom part of the card
        Row(
            modifier = Modifier
                .drawCardStroke(
                    corner = corner,
                    strokeColor = strokeColor,
                    strokeWidth = strokeWidth,
                    isTop = false
                )
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = (corner - 2).dp,
                        bottomEnd = (corner - 2).dp
                    )
                )
                .background(backgroundCard)
                .padding(bottom = 8.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = expiryDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// Extension function to draw the card stroke
private fun Modifier.drawCardStroke(
    corner: Float,
    strokeColor: Color,
    strokeWidth: Float,
    isTop: Boolean
) = this.drawBehind {
    val cornerRadius = corner.dp.toPx()
    val path = Path().apply {
        reset()
        if (isTop) {
            // Top part path
            moveTo(0f + cornerRadius, 0f)
            lineTo(size.width - cornerRadius, 0f)
            quadraticBezierTo(
                size.width, 0f,
                size.width, 0f + cornerRadius
            )
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            lineTo(0f, cornerRadius)
            quadraticBezierTo(
                0f, 0f,
                0f + cornerRadius, 0f
            )
        } else {
            // Bottom part path
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height - cornerRadius)
            quadraticBezierTo(
                size.width, size.height,
                size.width - cornerRadius, size.height
            )
            lineTo(cornerRadius, size.height)
            quadraticBezierTo(
                0f, size.height,
                0f, size.height - cornerRadius
            )
        }
        close()
    }

    drawPath(
        path = path,
        color = strokeColor,
        style = Stroke(width = strokeWidth.dp.toPx(), join = StrokeJoin.Round)
    )
}

@Composable
fun CardDiscountDividerView(
    strokeWidth: Float = 2f,
    strokeColor: Color = Color.Green
) {
    val backgroundCard: Color = MaterialTheme.colorScheme.background
    Row(
        Modifier
            .fillMaxWidth()
            .height(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CutOutShape(
            modifier = Modifier.width(16.dp),
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            backgroundColor = backgroundCard
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(color = backgroundCard),
            contentAlignment = Alignment.Center
        ) {
            DashedDivider(
                color = Color.Gray.copy(alpha = 0.3f),
                thickness = 1.dp,
                dashLength = 10f,
                gapLength = 16f
            )
        }

        CutOutShape(
            modifier = Modifier
                .width(16.dp)
                .graphicsLayer { scaleX = -1f },  // Flip horizontally
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            backgroundColor = backgroundCard
        )
    }
}

@Composable
fun DashedDivider(
    color: Color,
    thickness: Dp,
    dashLength: Float = 10f,
    gapLength: Float = 16f
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(thickness)
            .drawBehind {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = thickness.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(dashLength, gapLength),
                        0f
                    ),
                    cap = StrokeCap.Round
                )
            }
    )
}

@Composable
fun CutOutShape(
    modifier: Modifier,
    backgroundColor: Color = Color.White,
    strokeWidth: Float = 2f,
    strokeColor: Color = Color.Black,
) {
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(w, 0f)
            lineTo(w, h)
            lineTo(0f, h)

            // Semi-circle cutout curve
            cubicTo(
                0f, h * 14.8954f / 16f,
                w * 0.920007f / 16f, h * 14.0326f / 16f,
                w * 1.96379f / 16f, h * 13.6713f / 16f
            )
            cubicTo(
                w * 4.31307f / 16f, h * 12.8579f / 16f,
                w * 6f / 16f, h * 10.6259f / 16f,
                w * 6f / 16f, h * 8f / 16f
            )
            cubicTo(
                w * 6f / 16f, h * 5.37405f / 16f,
                w * 4.31307f / 16f, h * 3.14211f / 16f,
                w * 1.96379f / 16f, h * 2.32874f / 16f
            )
            cubicTo(
                w * 0.920006f / 16f, h * 1.96737f / 16f,
                0f, h * 1.10457f / 16f,
                0f, 0f
            )
            close()
        }

        // Draw background
        drawPath(
            path = path,
            color = backgroundColor,
        )

        // Draw stroke
        drawPath(
            path = path,
            color = strokeColor,
            style = Stroke(width = strokeWidth.dp.toPx(), join = StrokeJoin.Round)
        )
    }
}

@Composable
fun scaledDp(base: Dp): Dp {
    val fontScale = LocalDensity.current.fontScale * 0.8f
    return base * fontScale
}