package com.xmatmro.hskpractice.Screens

import android.app.Dialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HanZiMeaningScreen(
    level: Int,
    amount: Int,
    difficulty: Int
){
    val context = LocalContext.current
    var charactersList by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    var exerciseCharacters by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    val answersAmount = when (difficulty){
         1 -> 4
         2 -> 6
         3 -> 8
        else -> {3}
    }
    var currentTask by remember { mutableIntStateOf(0) }


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

                ) {
                    Text(text = exerciseCharacters[currentTask].hanzi,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineSmall)

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
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(allAnswers){  answer ->
                        AnswerCard(
                            answer.translations.take(3).joinToString(", \n"),
                            {
                                if(answer.id == currentCorrect.id){
                                    if(currentTask < exerciseCharacters.size - 1){
                                        currentTask++
                                    }
                                    else{
                                        currentTask = 0
                                    }
                                }
                            }
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
    onClick: () -> Unit
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        modifier = Modifier
            .clickable { onClick() }
            .height(130.dp)
        ,
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



