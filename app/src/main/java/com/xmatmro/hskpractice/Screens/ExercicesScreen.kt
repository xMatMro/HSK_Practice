package com.xmatmro.hskpractice.Screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xmatmro.hskpractice.Components.ExercicesCard
import com.xmatmro.hskpractice.Components.SegmentedControl
import com.xmatmro.hskpractice.Components.SegmentedControlButton
import com.xmatmro.hskpractice.Components.loadHSKData
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import kotlinx.serialization.json.Json
import kotlin.math.exp

@Composable
fun ExercicesScreen(
    level: Int,
    onFirstClick: (Int, Int, Int) -> Unit
) {
    val context = LocalContext.current
    var charactersList by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    val expanded = remember { mutableStateListOf<Boolean>().apply { repeat(7) { add(false) } } }
    var difficulty by remember { mutableStateOf(1) }
    var amountInput by remember { mutableStateOf("10") }
    val onAmountChange: (String) -> Unit = { input ->
        amountInput = input
    }
    val onDifficultyChange: (Int) -> Unit = { input ->
        difficulty = input
    }
    val onCardClick: (Int) -> Unit = { index ->
        for (i in expanded.indices) {
            if (i != index) {
                expanded[i] = false
            }
        }
        expanded[index] = !expanded[index]
    }


    LaunchedEffect(level) {
        charactersList = loadHSKData(context, level)
    }

    Surface(color= MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
            Text(
                text = "HSK $level",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))
            Text(
                text = "Wybierz ćwiczenie",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            ExercicesCard(
                "Znaczenie znaku",
                expanded[0],
                onCardClick,
                0,
                onFirstClick,
                amountInput,
                onAmountChange,
                level,
                difficulty,
                onDifficultyChange
            )

            ExercicesCard(
                "Pinyin znaku",
                expanded[1],
                onCardClick,
                1,
                onFirstClick,
                amountInput,
                onAmountChange,
                level,
                difficulty,
                onDifficultyChange
            )

            ExercicesCard(
                "Zdania z rozsypanki",
                expanded[2],
                onCardClick,
                2,
                onFirstClick,
                amountInput,
                onAmountChange,
                level,
                difficulty,
                onDifficultyChange
            )
        }
    }
}

private val hskJson = Json { ignoreUnknownKeys = true }


