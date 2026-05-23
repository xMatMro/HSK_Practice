package com.xmatmro.hskpractice.Screens

import android.app.Application
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.copy
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xmatmro.hskpractice.Components.HSKDescriptionCard
import com.xmatmro.hskpractice.Components.SegmentedControl
import com.xmatmro.hskpractice.Components.SegmentedControlButton
import com.xmatmro.hskpractice.ViewModels.GameViewModel

@Composable
fun HomeScreen(
    onStartClick: (Int) -> Unit
){
    val context = LocalContext.current
    val gameViewModel: GameViewModel = viewModel<GameViewModel>(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application))
    var level = gameViewModel.level
    val backgroundGradientColor = MaterialTheme.colorScheme.primaryContainer
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val backgroundGradientColor = MaterialTheme.colorScheme.primaryContainer
            val colorStops = arrayOf(
                0.0f to Color.Transparent,
                0.2f to backgroundGradientColor.copy(alpha = 0.4f),
                0.5f to backgroundGradientColor.copy(alpha = 0.9f),
                0.8f to backgroundGradientColor.copy(alpha = 0.4f),
                1f to Color.Transparent
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(60.dp)
                    .drawBehind{
                        drawRect(
                            brush = Brush.verticalGradient(
                                colorStops = colorStops,
                                startY = 0f,
                                endY = size.height,

                                ),
                            topLeft = Offset(size.width / 2 - size.width / 4, 0f),
                            size = Size(size.width / 2, size.height)
                        )
                    }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
//                    .drawBehind {
//                        drawCircle(
//                            brush = Brush.radialGradient(
//                                colors = listOf(
//                                    backgroundGradientColor,
//                                    Color.Transparent
//                                ),
//                                radius = size.minDimension * 0.6f,
//                                center = Offset(x = size.width / 2, y = size.height / 2)
//                            ),
//                            center = Offset(x = size.width / 2, y = size.height / 2),
//                            radius = size.minDimension * 0.6f
//                        )

//                    }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Witaj w HSK Practice!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = Bold
                )
                //Miał być dropdown, ale z pasji postanowił crashować aplikację
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Wybierz poziom HSK",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                    SegmentedControl {
                        listOf<Int>(1, 2, 3, 4, 5, 6).forEach { hskLevel ->
                            SegmentedControlButton(
                                onClick = {
                                    level = hskLevel
                                    gameViewModel.updateLevel(hskLevel)
                                },
                                text = hskLevel.toString(),
                                selected = level == hskLevel
                            )
                        }
                    }
                }

                Button(
                    onClick = { onStartClick(level) },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Start", fontSize = 18.sp, modifier = Modifier.padding(4.dp))

                }
                AnimatedContent(
                    targetState = level,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut())
                        } else {
                            (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> width } + fadeOut())
                        }
                    },
                    label = "HSKDescriptionCardAnimation"
                )
                { level ->
                    HSKDescriptionCard(
                        level = level
                    )
                }

            }
        }
    }


}


