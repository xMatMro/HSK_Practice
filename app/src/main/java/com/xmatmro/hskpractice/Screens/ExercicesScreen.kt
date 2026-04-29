package com.xmatmro.hskpractice.Screens

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xmatmro.hskpractice.Components.ExercicesCard
import com.xmatmro.hskpractice.Components.SegmentedControl
import com.xmatmro.hskpractice.Components.SegmentedControlButton
import com.xmatmro.hskpractice.Components.loadHSKData
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import com.xmatmro.hskpractice.ViewModels.GameViewModel
import kotlinx.serialization.json.Json
import java.util.Locale
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ExercicesScreen(
    level: Int,
    onFirstClick: (Int, Int, Int, Boolean) -> Unit,
    onSecondClick: (Int, Int, Int, Boolean) -> Unit,
    onThirdClick: (Int) -> Unit
) {
    val context = LocalContext.current
    var charactersList by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    val expanded = remember { mutableStateListOf<Boolean>().apply { repeat(7) { add(false) } } }
    var difficulty by remember { mutableStateOf(1) }
    var amountInput by remember { mutableStateOf("10") }
    val onAmountChange: (String) -> Unit = { input ->
        amountInput = input
    }

    val gameViewModel: GameViewModel = viewModel(viewModelStoreOwner = context as ViewModelStoreOwner)
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

            Column(
                modifier = Modifier
                    .padding(16.dp, 0.dp)
            ) {
                Text(
                    text = "Poziom trudności",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                SegmentedControl {
                    listOf(1, 2, 3).forEach { difficultyLevel ->
                        SegmentedControlButton(
                            onClick = { difficulty = difficultyLevel },
                            text = difficultyLevel.toString(),
                            selected = difficulty == difficultyLevel
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))




            Text(
                text = "Wybierz ćwiczenie",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))
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
                "hanZiMeaningScore",
                true,
                "pinyin"
            )

            ExercicesCard(
                "Pinyin znaku",
                expanded[1],
                onCardClick,
                1,
                onSecondClick,
                amountInput,
                onAmountChange,
                level,
                difficulty,
                "hanZiPinYinScore",
                true,
                "tłumaczenie"
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
                "hanZiMeaningScore",
                false,
                ""
            )
            Text(
                text = "Praktyka",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Button(
                onClick = {
                    onFirstClick(level, 10, difficulty,false)
                },
                modifier = Modifier
                    .padding(16.dp)

            ){
                Text(
                    text = "Zobacz rysowanie",

                )
            }


        }
    }
}
