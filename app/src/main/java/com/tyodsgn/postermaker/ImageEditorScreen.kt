package com.tyodsgn.postermaker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.DefaultStrokeLineMiter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tyodsgn.postermaker.ui.theme.fontHeadingFlex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.UUID
import kotlin.math.cos
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ImageEditorScreen(
    modiferTopAppBar: Modifier = Modifier, navController: NavController, imageName: String
){
    var isStickerSelectorOpen by remember { mutableStateOf(false) }

    var isTextEditorOpen by remember { mutableStateOf(false) }

    var textSticker by remember { mutableStateOf("") }

    var isProductStickerEditorOpen by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    val sheetStateProductSticker = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()
    
    val productStickerState = remember { mutableStateListOf<ProductStickerState>() }

    val textState = remember { mutableStateListOf<TextStickerState>() }

    val stickerStates = remember { mutableStateListOf<StickerState>() }

    var deletionZoneY by remember { mutableStateOf(0f) }
    var isOnDeletePresented by remember { mutableStateOf(false) }
    var canvasSize by remember { mutableStateOf<Size?>(null) }


    LaunchedEffect(key1 = Unit) {
        if (imageName == "bg"){
            val centerOffset = canvasSize?.let { size ->
                println(size.height)
                Offset((size.width / 4) - ( size.width * 0.08f), (size.height / 4) - ( size.height * 0.08f))
            } ?: Offset.Zero


            scope.launch {
                delay(200)
                isProductStickerEditorOpen =true
            }
        }
    }
    Scaffold(
        topBar = {
            ImageEditorTopBarView(
                navController = navController,
                modifier = modiferTopAppBar,
                stickerSelectorAction = {
                    isStickerSelectorOpen = true
                },
                textEditorAction = {
                    isTextEditorOpen = true
                }
            )
        },
        modifier = Modifier
            .fillMaxSize(),

    ) {
        val pad = it
        Column(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer
                )
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding()
                            + 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            // Canvas
            Box(
                Modifier
                    .clip(
                        RoundedCornerShape(24.dp)
                    )
                    .weight(1f)
                    .fillMaxWidth()
                    .onGloballyPositioned { layoutCoordinates ->
                        canvasSize = layoutCoordinates.size.toSize()
                    }


            ) {
                SharedTransitionLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    ImageBackgroundView(imageName)

                    //Display Product Sticker
                    productStickerState.forEach { product ->
                        DraggableProductStickerView(
                            productState = product,
                            onUpdate = { updatedState ->
                                val index = productStickerState.indexOf(product)
                                if (index != -1) productStickerState[index] = updatedState
                            })
                    }


                    // Display Stickers
                    stickerStates.forEach { sticker ->
                        DraggableStickerView(
                            stickerState = sticker,
                            onUpdate = { updatedState ->
                                val index = stickerStates.indexOf(sticker)
                                if (index != -1) stickerStates[index] = updatedState
                            },
                            onDelete = {
                                isOnDeletePresented = true
                                val stickerToRemove = stickerStates.find { it.id == sticker.id }
                                scope.launch {
                                    delay(500)
                                    stickerStates.remove(stickerToRemove).also {
                                        scope.launch {
                                            delay(500)
                                            isOnDeletePresented = false
                                        }
                                    }
                                }

                            },
                            deletionZoneY = deletionZoneY
                        )
                    }


                    // Display text
                    textState.forEach { text ->
                        val widthText = canvasSize?.width?.times(0.5) ?: 0.0
                        DraggableTextView(
                            textState = text,
                            onUpdate = { updatedState ->
                                val index = textState.indexOf(text)
                                if (index != -1) textState[index] = updatedState
                            },
                            onDelete = {
                                isOnDeletePresented = true
                                val stickerToRemove = textState.find { it.id == text.id }
                                scope.launch {
                                    delay(500)
                                    textState.remove(stickerToRemove).also {
                                        scope.launch {
                                            delay(500)
                                            isOnDeletePresented = false
                                        }
                                    }
                                }

                            },
                            deletionZoneY = deletionZoneY,
                            screenWidth = widthText.dp

                        )

                    }

                    // Deletion Zone
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .align(Alignment.BottomCenter)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = if (isOnDeletePresented) 0.6f else 0f))
                            .onGloballyPositioned { layoutCoordinates ->
                                deletionZoneY = layoutCoordinates.positionInRoot().y
                            }
                    ) {
                        Text(
                            text = "Drag here to delete",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(if (isOnDeletePresented) 1f else 0f),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    if (isTextEditorOpen){
                        // Display Add Text

                        Box (
                            Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter

                        ){
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(
                                        color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)
                                    )
                                    .clickable {
                                        isTextEditorOpen = false
                                        if (textSticker != "") {
                                            textState.add(
                                                TextStickerState(
                                                    text = textSticker,
                                                    offset = Offset(100f, 100f)
                                                )
                                            )
                                        }

                                        textSticker = ""
                                    }
                            )

                            Row(
                                Modifier
                                    .imePadding()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                CustomTextFieldView(
                                    text = textSticker,
                                    modifier = Modifier
                                        .weight(1f),
                                    onValueChange = {
                                        textSticker = it
                                    },
                                    onKeyboardDone = {
                                        isTextEditorOpen = false
                                        if (textSticker != "") {
                                            textState.add(
                                                TextStickerState(
                                                    text = textSticker,
                                                    offset = Offset(100f, 100f)
                                                )
                                            )
                                        }

                                        textSticker = ""
                                    }
                                )
                            }
                        }
                    }
                }

            }

            // Share Button
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
            ) {
                PrimaryButtonView(
                    label = "Share",
                    onClickButton = {

                    }
                )
            }


            
            if (isStickerSelectorOpen) {
                ModalBottomSheet(
                    onDismissRequest = {
                        isStickerSelectorOpen = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    StickerListView {
                        val centerOffset = canvasSize?.let { size ->
                            println(size.height)
                            Offset((size.width / 2) - (size.width * 0.11f), (size.height / 2) - (size.height * 0.08f))
                        } ?: Offset.Zero
                        stickerStates.add(StickerState(name = it, offset = centerOffset))
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                isStickerSelectorOpen = false
                            }
                        }
                    }
                }
            }

            if (isProductStickerEditorOpen){
                ModalBottomSheet(
                    onDismissRequest = {
                        isProductStickerEditorOpen = false
                    },
                    sheetState = sheetStateProductSticker,
                    modifier = Modifier
                ) {
                    ProductContentEditorView(
                        onUpdate = {
                            productStickerState.add(it)

                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    isProductStickerEditorOpen = false
                                }
                            }
                        },
                    )
                }
            }



        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StickerListView(
    onValueChange: (String) -> Unit
){
    FlowRow(
        maxItemsInEachRow = 3
    ) {
        listOfSticker.forEach {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        onValueChange(it)
                    },
                contentAlignment = Alignment.Center
            ) {
                StickerRowItemView(it)
            }
        }
    }
}


@Composable
fun StickerRowItemView(
    name: String
){
    val stickerId = mapSticker[name]

    if (stickerId != null){
        Image(
            painter = painterResource(id = stickerId),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp),
        )
    } else {
        Text("Sticker not found")
    }
}

fun Offset.rotateBy(angleDegrees: Float): Offset {
    val angleRadians = Math.toRadians(angleDegrees.toDouble())
    val cosAngle = cos(angleRadians).toFloat()
    val sinAngle = sin(angleRadians).toFloat()
    return Offset(
        x * cosAngle - y * sinAngle,
        x * sinAngle + y * cosAngle
    )
}

@Composable
fun DraggableProductStickerView(
    productState: ProductStickerState,
    onUpdate: (ProductStickerState) -> Unit
){
    var scale by remember { mutableStateOf(productState.scale) }
    var offset by remember { mutableStateOf(productState.offset) }
    var rotation by remember { mutableStateOf(productState.rotation) }

    StickerProductInfoView(
        name = productState.name,
        price = productState.price,
        discount = productState.discount,
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y,
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                        rotation += gestureRotate
                        scale *= gestureZoom

                        if (gestureRotate == 0f && gestureZoom == 1f) { // only allow movement if not rotating or zooming
                            offset += pan.rotateBy(rotation) * scale
                        }
                        onUpdate(
                            ProductStickerState(
                                name = productState.name,
                                price = productState.price,
                                discount = productState.discount,
                                offset = offset,
                                scale = scale,
                                rotation = rotation
                            )
                        )
                    },
                )
            }

    )
}

@Composable
fun ProductContentEditorView(
    onUpdate: (ProductStickerState) -> Unit = {},
    entryData: ProductStickerState = ProductStickerState(
        name = "NK 7328 Sumo - Benih Jagung Hibrida",
        price = "Rp120.000",
        discount = "Rp100.000"
    )
){
    var name by remember { mutableStateOf(entryData.name) }
    var originalPrice by remember { mutableStateOf(entryData.price) }
    var finalPrice by remember { mutableStateOf(entryData.discount) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)

    ) {
        Text(
            "Edit and change to your own pricing and discount to share with your customer",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        EditableStickerProductInfoView(
            onUpdate = {
                name = it.name
                originalPrice = it.price
                finalPrice = it.discount
            },
            onEditEntry = entryData
        )
        Spacer(modifier = Modifier.weight(1f))
        Box (
            Modifier.padding(top = 24.dp)
        ){
            PrimaryButtonView(
                label = "Save",
                onClickButton = {
                    onUpdate(
                        ProductStickerState(
                            name = name,
                            price = originalPrice,
                            discount = finalPrice
                        )
                    )
                }
            )
        }


    }
}

@Composable
fun EditIconSmallView(){
    Icon(
        Icons.Rounded.Edit,
        contentDescription = null,
        modifier = Modifier
            .offset(x = 12.dp)
            .clip(
                CircleShape
            )
            .background(
                color = MaterialTheme.colorScheme.primary
            )
            .size(28.dp)
            .padding(4.dp),
        tint = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun EditableStickerProductInfoView(
    onUpdate: (ProductStickerState) -> Unit = {},
    onEditEntry: ProductStickerState = ProductStickerState(
        name = "NK 7328 Sumo - Benih Jagung Hibrida",
        price = "120.000",
        discount = "100.000"
    ),
    modifier: Modifier = Modifier,
){
    var name by remember { mutableStateOf(onEditEntry.name) }
    var originalPrice by remember { mutableStateOf(onEditEntry.price) }
    var finalPrice by remember { mutableStateOf(onEditEntry.discount) }


    Box(
        Modifier
            .clip(
                RoundedCornerShape(36.dp)
            )
            .background(
                Color.Black
            )
            .padding(4.dp)
    ) {
        Column(
            modifier = modifier
                .padding(bottom = 16.dp)
                .width(240.dp)
                .shadow(
                    shape = RoundedCornerShape(32.dp),
                    elevation = 10.dp
                )
                .clip(
                    RoundedCornerShape(32.dp)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                )
                .verticalScroll(rememberScrollState())


        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                ProductImageView()
            }

            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {

                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    val focusManager = LocalFocusManager.current
                    BasicTextField(
                            value = name,
                            onValueChange = {
                                name = it
                                onUpdate(ProductStickerState(
                                    name = name,
                                    price = originalPrice,
                                    discount = finalPrice
                                ))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }),
                        )

                    EditIconSmallView()
                }

                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    val focusManager = LocalFocusManager.current
                    BasicTextField(
                        value = finalPrice,
                        onValueChange = {
                            finalPrice = it
                            onUpdate(ProductStickerState(
                                name = name,
                                price = originalPrice,
                                discount = finalPrice
                            ))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontFamily = fontHeadingFlex,
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize
                        ),

                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }),
                    )


                    EditIconSmallView()
                }

                Box(
                    contentAlignment = Alignment.TopEnd
                ) {
                    val focusManager = LocalFocusManager.current
                    BasicTextField(
                        value = originalPrice,
                        onValueChange = {
                            originalPrice = it
                            onUpdate(ProductStickerState(
                                name = name,
                                price = originalPrice,
                                discount = finalPrice
                            ))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            fontWeight = FontWeight.W600,
                            fontFamily = fontHeadingFlex,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.error,
                            textDecoration = TextDecoration.LineThrough
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }),

                    )
                    EditIconSmallView()
                }
            }


        }
    }
}

@Composable
fun DraggableStickerView(
    stickerState: StickerState,
    onUpdate: (StickerState) -> Unit,
    onDelete: () -> Unit,
    deletionZoneY: Float,
){
    val stickerId = mapSticker[stickerState.name]
    var scale by remember { mutableStateOf(stickerState.scale) }
    var offset by remember { mutableStateOf(stickerState.offset) }
    var rotation by remember { mutableStateOf(stickerState.rotation) }

    var isStickerInDeletionZone by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val dragThresholdPx = with(density) { 100.dp.toPx() }

    LaunchedEffect(offset.y) {
        onUpdate(stickerState.copy(offset = offset, scale = scale))
        isStickerInDeletionZone = offset.y + dragThresholdPx > deletionZoneY
        if (isStickerInDeletionZone) {
            onUpdate(StickerState(name = stickerState.name, offset = offset, scale = scale, rotation = rotation))
            onDelete()
        }
    }

    if (stickerId != null){
        Image(
            painter = painterResource(id = stickerId),
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y,
                )
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                            rotation += gestureRotate
                            scale *= gestureZoom

                            if (gestureRotate == 0f && gestureZoom == 1f) { // only allow movement if not rotating or zooming
                                offset += pan.rotateBy(rotation) * scale
                            }
//                            centroid = gestureCentroid
                            onUpdate(
                                StickerState(
                                    name = stickerState.name,
                                    offset = offset,
                                    scale = scale,
                                    rotation = rotation
                                )
                            )
                        },
                    )
                }
        )
    }

}

@Composable
fun DraggableTextView(
    textState: TextStickerState,
    onUpdate: (TextStickerState) -> Unit,
    onDelete: () -> Unit,
    deletionZoneY: Float,
    screenWidth: Dp,
    modifier: Modifier = Modifier
){
    var scale by remember { mutableStateOf(textState.scale) }
    var offset by remember { mutableStateOf(textState.offset) }
    var rotation by remember { mutableStateOf(textState.rotation) }

    var isStickerInDeletionZone by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val dragThresholdPx = with(density) { 100.dp.toPx() }

    LaunchedEffect(offset.y) {
        onUpdate(textState.copy(offset = offset, scale = scale))
        isStickerInDeletionZone = offset.y + dragThresholdPx > deletionZoneY
        if (isStickerInDeletionZone) {
            onUpdate(TextStickerState(text = textState.text, offset = offset, scale = scale, rotation = rotation))
            onDelete()
        }
    }
    Box(
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y,
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                        rotation += gestureRotate
                        scale *= gestureZoom

                        if (gestureRotate == 0f && gestureZoom == 1f) { // only allow movement if not rotating or zooming
                            offset += pan.rotateBy(rotation) * scale
                        }
                        onUpdate(
                            TextStickerState(
                                text = textState.text,
                                offset = offset,
                                scale = scale,
                                rotation = rotation
                            )
                        )
                    },
                )
            }
    ){
        TextStickerView(text = textState.text, screenWidth = screenWidth)
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CustomTextFieldView(
    text: (String) = "Cool",
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onKeyboardDone: () -> Unit = {}
){
    val focusManager = LocalFocusManager.current
    val fWeight = FontWeight.W600
    val fSize = MaterialTheme.typography.headlineLarge.fontSize
    val fSpacing = 0.1.sp
    val fAlign = TextAlign.Center
    val fFamily = fontHeadingFlex

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester)
            .fillMaxWidth(),
        textStyle = TextStyle(
            color = Color.White.copy(alpha = 0.0f),
            fontSize = fSize,
            fontWeight = fWeight,
            lineHeight = MaterialTheme.typography.headlineLarge.lineHeight,
            fontFamily = fFamily,
            fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
            textAlign = fAlign,
            letterSpacing = fSpacing,
            drawStyle = Stroke(
                miter = DefaultStrokeLineMiter,
                width = 8f,
                join = StrokeJoin.Round
            ),
        ),
        cursorBrush = SolidColor(Color.White.copy(alpha = 0.8f)),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onKeyboardDone()
            }),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp
                )

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
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
                            )
                        ),
                        letterSpacing = fSpacing,
                        textAlign = fAlign,
                        fontFamily = fFamily,
                    )

                    Text(text = text,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = fSize,
                            fontWeight = fWeight,
                            textAlign = fAlign,
                            letterSpacing = fSpacing,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(0.0f, 10.0f),
                                blurRadius = 0.0f
                            ),
                            fontFamily = fFamily,
                        )
                    )

                    innerTextField()
                }
            }

        }
    )
}

@Composable
fun ImageEditorTopBarView(
    navController: NavController,
    modifier: Modifier = Modifier,
    stickerSelectorAction: () -> Unit = {},
    textEditorAction: () -> Unit = {},
){
    Row(
        modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .fillMaxWidth()
//            .padding(horizontal = 16.dp)
            .padding(
                top = 12.dp,
                start = 8.dp,
                end = 16.dp,
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        // Nav Button
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ){
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
            Text(
                "Edit Poster",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        // Action Button

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            IconButton(
                onClick = stickerSelectorAction,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.sticker) ,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            IconButton(
                onClick = textEditorAction,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface
                    )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.type),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }


    }

}

@Composable
fun CustomActionView(){

}


@Composable
fun PrimaryButtonView(
    onClickButton: () -> Unit,
    label: String = "Share Poster"
){
    Button(
        onClick = { onClickButton() },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = true,
        shape = CircleShape,
    ) {
        Text(label,
            style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun ImageBackgroundView(name: String){
    val imageId = drawableMap[name]

    if (imageId != null) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(1f)
        )
    }
}


@Composable
fun OverlayTextView(){

}

data class TextStickerState(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val offset: Offset = Offset.Zero,
    val scale: Float = 1.0f,
    val rotation: Float = 0f,
)

data class StickerState(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val offset: Offset = Offset.Zero,
    val scale: Float = 1.5f,
    val rotation: Float = 0f,
)

data class ProductStickerState(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val price: String = "0",
    val discount: String = "0",
    val offset: Offset = Offset.Zero,
    val scale: Float = 0.8f,
    val rotation: Float = 0f,
)

val listOfSticker = listOf(
    "stickercelebrate",
    "stickerconfused",
    "stickercool",
    "stickerhaha",
    "stickerhug",
    "stickerpeace",
    "stickersurprised",
    "stickerthumbsup",
    "stickerthanks"
)

val mapSticker = mapOf(
    "stickercelebrate" to R.drawable.stickercelebrate,
    "stickerconfused" to R.drawable.stickerconfused,
    "stickercool" to R.drawable.stickercool,
    "stickerhaha" to R.drawable.stickerhaha,
    "stickerhug" to R.drawable.stickerhug,
    "stickerpeace" to R.drawable.stickerpeace,
    "stickersurprised" to R.drawable.stickersurprised,
    "stickerthumbsup" to R.drawable.stickerthumbsup,
    "stickerthanks" to R.drawable.stickerthanks
)

@Preview(showBackground = false)
@Composable
fun PreviewImageEditorScreen(){
    ImageEditorScreen(
        navController = rememberNavController(),
        imageName = "p4"
    )
}
