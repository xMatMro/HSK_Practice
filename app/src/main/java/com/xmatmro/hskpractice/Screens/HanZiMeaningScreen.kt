package com.xmatmro.hskpractice.Screens

import android.R.attr.text
import android.app.Application
import android.app.Dialog
import android.content.Context
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.xmatmro.hskpractice.Components.loadHSKData
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import kotlinx.serialization.json.Json
import kotlin.random.Random
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xmatmro.hskpractice.ViewModels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HanZiMeaningScreen(
    level: Int,
    amount: Int,
    difficulty: Int,
    back: () -> Unit,
    checked: Boolean
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var charactersList by rememberSaveable { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    var exerciseCharacters by rememberSaveable { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }
    val answersAmount = when (difficulty){
         1 -> 4
         2 -> 6
         3 -> 8
        else -> {3}
    }
    var currentTask by rememberSaveable { mutableIntStateOf(0) }
    var progressCurrentTask by rememberSaveable { mutableIntStateOf(0) }
    var isAnswerVisible by remember(currentTask) { mutableStateOf(false) }

    val gameViewModel: GameViewModel = viewModel<GameViewModel>(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application))
    var points by rememberSaveable() { mutableIntStateOf(0) }
    val animatedProgress by animateFloatAsState(
        targetValue =  if (exerciseCharacters.isNotEmpty()) progressCurrentTask.toFloat()  / exerciseCharacters.size else 0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "progress"

    )
    var text by rememberSaveable {mutableStateOf("") }
    var textPinYin by remember { mutableStateOf(false) }
    var slideIn by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(level) {
        if(exerciseCharacters.isEmpty()){
            val loadedData = loadHSKData(context, level)
            if (loadedData.isNotEmpty()) {
                charactersList = loadedData
                exerciseCharacters = loadedData.shuffled().take(amount.coerceAtMost(loadedData.size))
                currentTask = 0
                progressCurrentTask = 0
                text = ""
            }
        }

    }
if(slideIn){
    WinningScreen(
        points = points,
        amount = amount,
        difficulty = difficulty,
        back = back,
        time = 0,
        slideIn = slideIn

    )
}else {
    Surface(color = MaterialTheme.colorScheme.background) {
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
                    .drawBehind {
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
            if (exerciseCharacters.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                text = exerciseCharacters[currentTask].hanzi
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally


                ) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(250.dp)
                            .height(15.dp),
                        color = ProgressIndicatorDefaults.linearColor,
                        trackColor = ProgressIndicatorDefaults.linearTrackColor,
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                        gapSize = (-15).dp,
                        drawStopIndicator = {}
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color.Black),


                        ) {
                        Column(
                            modifier = Modifier,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = text,
                                modifier = Modifier
                                    .padding(16.dp, 4.dp)
                                    .animateContentSize()
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = {
                                            if (!checked) {
                                                text = if (textPinYin) {
                                                    exerciseCharacters[currentTask].hanzi
                                                } else {
                                                    "${exerciseCharacters[currentTask].hanzi}\n${exerciseCharacters[currentTask].pinyin}"
                                                }
                                                textPinYin = !textPinYin
                                            }
                                        }),
                                style = (MaterialTheme.typography.headlineSmall),
                                textAlign = TextAlign.Center
                            )

                            if (checked) {
                                Text(
                                    text = exerciseCharacters[currentTask].pinyin,
                                    modifier = Modifier.padding(16.dp, 4.dp),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }


                    }
                    AnimatedVisibility(isAnswerVisible) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp, Color.Green),


                            ) {
                            Text(
                                text = exerciseCharacters[currentTask].translations[0],
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleLarge
                            )

                        }

                    }
                    val currentCorrect = exerciseCharacters[currentTask]
                    val wrongAnswers = remember(currentTask) {
                        charactersList.filter { it.id != currentCorrect.id }.shuffled()
                            .take(answersAmount - 1)

                    }
                    val allAnswers = remember(currentTask) {
                        (wrongAnswers + currentCorrect).shuffled()
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(allAnswers) { answer ->
                            AnswerCard(
                                answer.translations.take(3).joinToString(", \n"),
                                !isProcessing,
                                { correct ->
                                    isProcessing = true
                                    if (!correct) {
                                        isAnswerVisible = true
                                    }
                                    if (progressCurrentTask < exerciseCharacters.size) {
                                        progressCurrentTask++

                                    }

                                    scope.launch {
                                        if (correct) {
                                            delay(1000)
                                        } else {
                                            delay(1500)
                                            isAnswerVisible = false
                                        }
                                        delay(500)
                                        if (answer.id == currentCorrect.id) {
                                            points++
                                        }
                                        if (currentTask < exerciseCharacters.size - 1) {
                                            currentTask++


                                        } else {
                                            gameViewModel.addPoints(
                                                "hanZiMeaningScore",
                                                points,
                                                amount
                                            )
                                            slideIn = true
                                        }
                                        isProcessing = false
                                    }


                                },
                                correct = answer.id == currentCorrect.id
                            )

                        }

                    }

                }
            }
        }
    }
}
}

@Composable
fun AnswerCard(
    text: String,
    isEnabled: Boolean,
    onClick:  (Boolean) -> Unit,
    correct: Boolean
    ){
    var clicked by remember(text) { mutableStateOf(false) }
    val borderColor = when{
        clicked && correct -> Color.Green
        clicked && !correct -> Color.Red
        else -> Color.Black
    }
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .padding(top=8.dp)
            .clickable(enabled = isEnabled && !clicked) {
                clicked = true
                scope.launch{
                    launch{
                        if(correct){
                            offsetY.animateTo(-60f, animationSpec = tween(125) )
                            offsetY.animateTo(40f, animationSpec = tween(125))
                            offsetY.animateTo(-15f, animationSpec = tween(125))
                            offsetY.animateTo(5f, animationSpec = tween(125))
                            offsetY.animateTo(0f, animationSpec = tween(125))

                        }
                        else{
                            offsetX.animateTo(-60f, animationSpec = tween(75))
                            offsetX.animateTo(55f, animationSpec = tween(75))
                            offsetX.animateTo(-45f, animationSpec = tween(75))
                            offsetX.animateTo(40f, animationSpec = tween(75))
                            offsetX.animateTo(-25f, animationSpec = tween(75))
                            offsetX.animateTo(20f, animationSpec = tween(75))
                            offsetX.animateTo(-10f, animationSpec = tween(75))
                            offsetX.animateTo(5f, animationSpec = tween(75))
                            offsetX.animateTo(0f, animationSpec = tween(75))
                        }


                    }
                    onClick(correct)
                }
            }
            .height(130.dp)
            .offset { IntOffset(offsetX.value.roundToInt(),offsetY.value.roundToInt()) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),

        ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text = text,modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
        }



    }
}



