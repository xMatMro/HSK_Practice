package com.xmatmro.hskpractice.Screens

import android.view.Surface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.round

@Composable
fun WinningScreen(
    points: Int,
    amount: Int,
    difficulty: Int,
    back: () -> Unit,
    time: Int,
    slideIn: Boolean
){
    var isVisible by remember { mutableStateOf(false) }
    val targetScore = if (amount > 0) (points.toFloat() / amount) * 100 else 0f
    val animatedScore by animateFloatAsState(
        targetValue = if (slideIn) targetScore else 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "scoreAnimation"
    )
    LaunchedEffect(Unit) {
        delay(500)
        isVisible = true
    }
    Surface(color = MaterialTheme.colorScheme.background){
        Box(modifier = Modifier.fillMaxSize()){
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
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(vertical = 80.dp, horizontal = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.4f),
                    )
                    .clip(RoundedCornerShape(20.dp))
                    .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha=0.75f), RoundedCornerShape(20.dp))
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn(tween(1000))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,

                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Gratulacje!!!",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(16.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
                        Text(
                            text = "Poziom ukończony",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                        Box(
                            modifier = Modifier.size(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { animatedScore / 100 },
                                modifier = Modifier.size(200.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 16.dp,
                                trackColor = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
                                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                            )
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "${animatedScore.toInt()}%",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "$points/$amount",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically ( initialOffsetY = { 50 }) + fadeIn(tween(1000))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                back() },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                ),
                            ){
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}