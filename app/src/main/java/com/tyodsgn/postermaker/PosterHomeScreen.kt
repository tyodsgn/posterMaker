package com.tyodsgn.postermaker

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tyodsgn.postermaker.ui.theme.PosterMakerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosterHomeScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Text("Poster Maker")
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        val pad = it
        Column (
            modifier
                .fillMaxHeight()
                .verticalScroll(
                    state = scrollState,
                    enabled = true,
                )
                .padding(
                    top = it.calculateTopPadding()
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(
                        bottom = 80.dp
                    )
            ) {
                PosterHorizontalSectionView(
                    sectionTitle = "",
                    isLandscape = true,
                    navController = navController
                )
                PosterHorizontalSectionView(
                    sectionTitle = "Made by you",
                    navController = navController
                )
                PosterHorizontalSectionView(
                    sectionTitle = "Seasonal Poster",
                    filter = "greeting",
                    navController = navController
                )

                PosterHorizontalSectionView(
                    sectionTitle = "Currently Trending",
                    filter = "greeting",
                    navController = navController
                )

                PosterVerticalSectionView(
                    sectionTitle = "All Poster",
                    filter = "all",
                    navController = navController
                )
            }

        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PosterVerticalSectionView(
    sectionTitle: String = "Seasonal Poster",
    dataPoster: List<DataPoster> = sampleData,
    filter: String = "any",
    navController: NavHostController,
) {
    val filteredData = if (filter == "all") dataPoster else dataPoster.filter { it.tag == filter }
    val totalColumn = 2

    Column {
        if (sectionTitle != "") {
            Text(
                sectionTitle,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                    ),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        FlowRow(
            modifier = Modifier
                .padding( 16.dp)
                .fillMaxWidth(),
            maxItemsInEachRow = totalColumn,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                val cardHeight = 240.dp
                filteredData.forEach {
                    PosterThumbnail(
                        it.name,
                        modifier = Modifier
                            .weight(1f)
                            .height(cardHeight)
                            .clickable {
                                navController.navigate("imageEditorScreen/${it.name}")
                            }
                    )
                }
                if (filteredData.size % totalColumn != 0) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(cardHeight)
                            .alpha(0.0f)
                    )
                }
            }
        )
    }
}

@Composable
fun PosterHorizontalSectionView(
    sectionTitle: String = "Seasonal Poster",
    dataPoster: List<DataPoster> = sampleData,
    filter: String = "any",
    isLandscape: Boolean = false,
    navController: NavHostController
){
    val filteredData = dataPoster.filter { it.tag == filter }

    Column() {
        if (sectionTitle != "") {
            Text(
                sectionTitle,
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp,
                    ),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Row(
            Modifier
                .horizontalScroll(
                    state = rememberScrollState(),
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),

        ){
            filteredData.forEach {
                PosterThumbnail(
                    it.name,
                    isLandscape = isLandscape,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("imageEditorScreen/${it.name}")
                        })
            }
        }
    }
}

@Composable
fun PosterThumbnail(
    name: String,
    isLandscape: Boolean = false,
    width: Dp = 0.dp,
    height: Dp = 0.dp,
    modifier: Modifier = Modifier,
){

    val imageId = drawableMap[name]
    val imageHeight = if (height == 0.dp) (if (isLandscape) 140.dp else 180.dp) else height
    val imageWidth = if (width == 0.dp) (if (isLandscape) 220.dp else 120.dp) else width

    Box(
        modifier
            .clip(
                RoundedCornerShape(16.dp)
            )
            .width(imageWidth)
            .height(imageHeight)
    ) {
        if (imageId != null) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Gray.copy(alpha = 0.5f)
                    )

            ){
                Text("Image not loaded")
            }
        }
    }
}

@Composable
fun PosterLandscapeThumbnail(
    image: Int = R.drawable.p1
){
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = Modifier
            .fillMaxWidth(fraction = 0.5f)
            .fillMaxHeight(fraction = 0.15f)
            .clip(
                RoundedCornerShape(16.dp)
            )

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = false)
@Composable
fun PreviewThumbnailPortrait() {
    PosterMakerTheme {
        PosterHomeScreen(navController = rememberNavController())
    }

}


data class DataPoster(
    val name: String,
    val tag: String,
)

val sampleData: List<DataPoster> = listOf(
    DataPoster("p1", "any"),
    DataPoster("p2", "any"),
    DataPoster("p3", "any"),
    DataPoster("p4", "any"),
    DataPoster("p5", "any"),
    DataPoster("p6", "any"),
    DataPoster("p7", "any"),
    DataPoster("p8", "any"),
    DataPoster("p9", "any"),
    DataPoster("s1", "greeting"),
    DataPoster("s2", "greeting"),
    DataPoster("s3", "greeting"),
    DataPoster("s4", "greeting"),
)

// Predefined mapping
val drawableMap = mapOf(
    "p1" to R.drawable.p1,
    "p2" to R.drawable.p2,
    "p3" to R.drawable.p3,
    "p4" to R.drawable.p4,
    "p5" to R.drawable.p5,
    "p6" to R.drawable.p6,
    "p7" to R.drawable.p7,
    "p8" to R.drawable.p8,
    "p9" to R.drawable.p9,
    "s1" to R.drawable.s1,
    "s2" to R.drawable.s2,
    "s3" to R.drawable.s3,
    "s4" to R.drawable.s4,
    "bg" to R.drawable.bg
)