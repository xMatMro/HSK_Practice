package com.xmatmro.hskpractice.Screens

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xmatmro.hskpractice.Components.loadHSKData
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import com.xmatmro.hskpractice.ViewModels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.plus
import kotlin.collections.shuffled
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HanZiPinYinScreen(
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

    val gameViewModel: GameViewModel = viewModel<GameViewModel>(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application))
    var points by remember { mutableIntStateOf(0) }
    val animatedProgress by animateFloatAsState(
        targetValue =  if (exerciseCharacters.isNotEmpty()) progressCurrentTask.toFloat()  / exerciseCharacters.size else 0f,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "progress"

    )
    var text by rememberSaveable {mutableStateOf("") }
    var textTranslation by remember { mutableStateOf(false) }

    LaunchedEffect(level) {
        if(exerciseCharacters.isEmpty()){
            val loadedData = loadHSKData(context, level)
            if (loadedData.isNotEmpty()) {
                charactersList = loadedData
                exerciseCharacters = loadedData.shuffled().take(amount.coerceAtMost(loadedData.size))
                currentTask = 0
                progressCurrentTask = 0
            }
        }

    }

    Surface(color = MaterialTheme.colorScheme.background) {
        if(exerciseCharacters.isEmpty()){
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else{
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
                        .padding(top=16.dp)
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
                    border = BorderStroke(1.dp,Color.Black),
                    modifier = Modifier.padding(16.dp)



                    ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = text,
                            modifier = Modifier
                                .padding(16.dp,4.dp)
                                .animateContentSize()
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        if(!checked){
                                            text = if(textTranslation){
                                                exerciseCharacters[currentTask].hanzi
                                            } else{
                                                "${exerciseCharacters[currentTask].hanzi}\n${exerciseCharacters[currentTask].translations[0]}"
                                            }
                                            textTranslation = !textTranslation
                                        }

                                    }
                                ),
                            style = (MaterialTheme.typography.headlineSmall),
                            textAlign = TextAlign.Center
                            )

                        if(checked){
                            Text(text = exerciseCharacters[currentTask].translations[0],
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
                        modifier = Modifier.padding(16.dp)


                        ) {
                        Text(text = exerciseCharacters[currentTask].pinyin,
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
                            answer.pinyin,
                            !isProcessing,
                            {correct ->
                                isProcessing = true
                                if(!correct){
                                    isAnswerVisible = true

                                }
                                if(progressCurrentTask < exerciseCharacters.size){
                                    progressCurrentTask++
                                }
                                scope.launch {
                                    if(correct){
                                        delay(1000)
                                    }else{
                                        delay(1500)
                                        isAnswerVisible = false
                                    }
                                    delay(500)
                                    if(answer.id == currentCorrect.id){
                                        points++
                                    }
                                    if(currentTask < exerciseCharacters.size - 1){
                                        currentTask++


                                    } else {
                                        gameViewModel.addPoints("hanZiTranslationScore",points,amount)
                                        exerciseCharacters = emptyList()
                                        charactersList = emptyList()
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