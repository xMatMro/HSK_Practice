package com.xmatmro.hskpractice.Screens

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xmatmro.hskpractice.Components.loadHSKData
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import com.xmatmro.hskpractice.HSKCharacters.HSKSentence
import com.xmatmro.hskpractice.ViewModels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.math.ceil

@Composable
fun SentencesScreen(
    level: Int,
    amount: Int,
    difficulty: Int,
    checked: Boolean,
    back: () -> Unit
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var charactersList by rememberSaveable { mutableStateOf<List<HSKSentence>>(emptyList()) }
    var exerciseCharacters by rememberSaveable { mutableStateOf<List<HSKSentence>>(emptyList()) }
    var currentTask by rememberSaveable { mutableStateOf(0) }
    var progressCurrentTask by rememberSaveable { mutableStateOf(0) }
    val blockAmount = when(difficulty){
        1 -> 4
        2 -> 5
        3 -> 6
        else -> 4
    }
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
    var isAnswerVisible by remember(currentTask) { mutableStateOf(false) }
    var order by rememberSaveable { mutableStateOf(emptyList<Int>()) }
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    var slideIn by rememberSaveable {mutableStateOf(false) }
    LaunchedEffect(level) {
        if(exerciseCharacters.isEmpty()){
            val fileName = "hskSentences.json"
            val inputStream = context.assets.open(fileName)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val loadedData = Json.decodeFromString<Map<String, List<HSKSentence>>>(jsonString)
            if (loadedData.isNotEmpty()) {
                charactersList = loadedData["HSK$level"]!!
                exerciseCharacters = charactersList.shuffled().take(amount.coerceAtMost(loadedData.size))
                currentTask = 0
                progressCurrentTask = 0
            }
        }
    }
    if(slideIn){
        WinningScreen(
            points,
            amount,
            difficulty,
            back,
            0,
            slideIn
        )}
        else{
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
                if (exerciseCharacters.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
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
                        text = exerciseCharacters[currentTask].translation
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            border = BorderStroke(1.dp,Color.Black),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = text,
                                    modifier = Modifier
                                        .padding(16.dp,4.dp)
                                        .animateContentSize()
                                        .clickable(
                                            indication = null,
                                            interactionSource = remember{ MutableInteractionSource() },
                                            onClick = {
                                                if(!checked){
                                                    text = if(!textPinYin){
                                                        "${exerciseCharacters[currentTask].translation}\n${exerciseCharacters[currentTask].pinyinSentence}"
                                                    }else{
                                                        exerciseCharacters[currentTask].translation
                                                    }
                                                    textPinYin = !textPinYin
                                                }
                                            }
                                        ),
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                                if(checked){
                                    Text(
                                        text = exerciseCharacters[currentTask].pinyinSentence,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(16.dp,4.dp)
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
                                border = BorderStroke(1.dp,Color.Green),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text=exerciseCharacters[currentTask].hanziSentence,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        val trimmed = remember(currentTask) {if(exerciseCharacters[currentTask].hanziSentence[exerciseCharacters[currentTask].hanziSentence.length-1] == '。'){
                            exerciseCharacters[currentTask].hanziSentence.replace("。","")
                        }else{
                            exerciseCharacters[currentTask].hanziSentence
                        }  }

                        val chunkSize = remember(currentTask) { ceil(trimmed.length.toDouble() / blockAmount).toInt().coerceAtLeast(1) }
                        val blocks = remember(currentTask) { if(trimmed.length>=blockAmount){
                            trimmed.chunked(chunkSize)
                        } else{
                            trimmed.chunked(trimmed.length)
                        }
                        }
                        val shuffledBlock = remember(currentTask){
                            blocks.mapIndexed { index, s -> index to s }.shuffled()
                        }

                        var correctOrder by rememberSaveable(currentTask) { mutableStateOf(List(blocks.size){it}) }
                        var answeredCorrectly by rememberSaveable(currentTask) { mutableIntStateOf(1) }
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                            ,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ){
                            items(shuffledBlock.size){ index ->
                                SentencePartCard(
                                    id = shuffledBlock[index].first,
                                    text = shuffledBlock[index].second,
                                    addToOrder = {id ->
                                        order = if(!order.contains(id)){
                                            order + id
                                        } else{
                                            order - id
                                        }
                                    },
                                    isEnabled = isEnabled,
                                    answeredCorrectly = answeredCorrectly

                                )

                            }

                        }
                        Button(
                            modifier = Modifier.padding(16.dp),
                            onClick = {
                                isEnabled = false
                                if(order != correctOrder){
                                    isAnswerVisible = true
                                    answeredCorrectly = 2
                                }else{
                                    answeredCorrectly = 3
                                }
                                if(progressCurrentTask < exerciseCharacters.size){
                                    progressCurrentTask++
                                }
                                scope.launch {
                                    if(order == correctOrder){
                                        points++
                                        order = emptyList()
                                        delay(1000)
                                    }else{
                                        delay(1500)
                                        isAnswerVisible = false
                                    }
                                    delay(500)
                                    answeredCorrectly = 1
                                    if(currentTask < exerciseCharacters.size - 1) {
                                        currentTask++
                                    }else{
                                        slideIn = true
                                        gameViewModel.addPoints("sentencesScore",points,amount)

                                    }

                                    isEnabled = true
                                }
                            }
                        ){
                            Text(
                                text = "Zatwierdź",
                                fontSize = 18.sp,
                                modifier = Modifier.padding(4.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                    }
                }
            }
        }

    }

}
@Composable
fun SentencePartCard(
    id: Int,
    text: String,
    addToOrder: (Int) -> Unit,
    isEnabled: Boolean,
    answeredCorrectly: Int,
){
    var clicked by remember(text) { mutableStateOf(false) }
    val borderColor = when(answeredCorrectly){
         1 -> if(clicked) MaterialTheme.colorScheme.primary else Color.Black
         2 -> Color.Red
         3 -> Color.Green
        else -> Color.Black
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .padding(6.dp)
            .height(90.dp)
            .clickable(
                indication = null,
                interactionSource = remember{ MutableInteractionSource() },
                enabled = isEnabled,
                onClick = {
                    addToOrder(id)
                    clicked = !clicked
                }
            ),
        border = BorderStroke(if(clicked && answeredCorrectly == 1)2.dp else 1.dp,borderColor),
    ){
        Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
        ){
            Text(text = text,modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center,style = MaterialTheme.typography.titleLarge)
        }
    }

}