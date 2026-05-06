package com.xmatmro.hskpractice.Screens

import android.app.Dialog
import android.content.Context
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.material3.adaptive.layout.PaneExpansionAnchor
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xmatmro.hskpractice.ViewModels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
    var charactersList by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    var exerciseCharacters by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    var isProcessing by remember { mutableStateOf(false) }
    val answersAmount = when (difficulty){
         1 -> 4
         2 -> 6
         3 -> 8
        else -> {3}
    }
    var currentTask by remember { mutableIntStateOf(0) }
    var isAnswerVisible by remember(currentTask) { mutableStateOf(false) }

    val gameViewModel: GameViewModel = viewModel(viewModelStoreOwner = context as ViewModelStoreOwner)
    var points by remember { mutableIntStateOf(0) }


    LaunchedEffect(level) {
        val loadedData = loadHSKData(context, level)
        if (loadedData.isNotEmpty()) {
            charactersList = loadedData
            exerciseCharacters = loadedData.shuffled().take(amount.coerceAtMost(loadedData.size))
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        if(exerciseCharacters.isEmpty()){
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally


            ) {
                Text("Zadanie ${currentTask + 1}/${exerciseCharacters.size}",style=MaterialTheme.typography.headlineSmall)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp,Color.Black),


                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = exerciseCharacters[currentTask].hanzi,
                            modifier = Modifier.padding(16.dp,4.dp),
                            style = (MaterialTheme.typography.headlineSmall),

                            )

                        if(checked){
                            Text(text = exerciseCharacters[currentTask].pinyin,
                                modifier = Modifier.padding(16.dp,4.dp),
                                style = MaterialTheme.typography.titleMedium)
                        }
                    }


                }
                AnimatedVisibility(isAnswerVisible) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp,Color.Green),


                        ) {
                        Text(text = exerciseCharacters[currentTask].translations[0],
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleLarge)

                    }

                }
                val currentCorrect = exerciseCharacters[currentTask]
                val wrongAnswers = remember(currentTask){
                    charactersList.filter { it.id != currentCorrect.id }.shuffled().take(answersAmount - 1)

                }
                val allAnswers = remember(currentTask){
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
                    items(allAnswers){  answer ->
                        AnswerCard(
                            answer.translations.take(3).joinToString(", \n"),
                            !isProcessing,
                            {correct ->
                                isProcessing = true
                                if(!correct){
                                    isAnswerVisible = true
                                }

                                scope.launch {
                                    delay(1500)
                                    isAnswerVisible = false
                                    delay(500)
                                    if(answer.id == currentCorrect.id){
                                        points++
                                    }
                                    if(currentTask < exerciseCharacters.size - 1){
                                        currentTask++


                                    } else {
                                        gameViewModel.addPoints("hanZiMeaningScore",points,amount)
                                        back()
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
            .clickable(enabled = isEnabled && !clicked) {
                clicked = true
                scope.launch{
                    launch{
                        if(correct){
                            offsetY.animateTo(60f, animationSpec = tween(125) )
                            offsetY.animateTo(-40f, animationSpec = tween(125))
                            offsetY.animateTo(15f, animationSpec = tween(125))
                            offsetY.animateTo(-5f, animationSpec = tween(125))
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



