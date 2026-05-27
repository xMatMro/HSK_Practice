package com.xmatmro.hskpractice.Screens

import android.app.Application
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
import android.widget.Space
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModelProvider

@Composable
fun ExercicesScreen(
    level: Int,
    onFirstClick: (Int, Int, Int, Boolean) -> Unit,
    onSecondClick: (Int, Int, Int, Boolean) -> Unit,
    onThirdClick: (Int, Int, Int, Boolean) -> Unit,
    onFourthClick: (Int, Int, Int, Boolean) -> Unit,
    onFifthClick: (Int, Int, Int, Boolean) -> Unit,
) {
    val context = LocalContext.current
    var charactersList by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }
    val expanded = remember { mutableStateListOf<Boolean>().apply { repeat(7) { add(false) } } }



    val gameViewModel: GameViewModel = viewModel<GameViewModel>(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application))
    var difficulty = gameViewModel.difficulty
    var amountInput = gameViewModel.amount
    val onAmountChange: (String) -> Unit = { input ->
        amountInput = input
        gameViewModel.updateSettings(difficulty,amountInput)
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
                    .statusBarsPadding()
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),

                ) {
                Text(
                    text = "HSK $level",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                ) {
                    Text(
                        text = "Difficulty",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    SegmentedControl {
                        listOf(1, 2, 3).forEach { difficultyLevel ->
                            SegmentedControlButton(
                                onClick = {
                                    difficulty = difficultyLevel
                                    gameViewModel.updateSettings(difficulty, amountInput)
                                },
                                text = difficultyLevel.toString(),
                                selected = difficulty == difficultyLevel
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Learning",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )
                ExercicesCard(
                    title = "Learning characters",
                    expanded[4],
                    onCardClick,
                    4,
                    onFifthClick,
                    amountInput,
                    onAmountChange,
                    level,
                    difficulty,
                    false,
                    "",
                    false,
                    helpText = "",
                    false,
                    "Learn all the characters from chosen HSK level, use text to speech to learn pronunciation and see how to write them!"
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Practice",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )


                ExercicesCard(
                    title = "Practice drawing",
                    expanded[3],
                    onCardClick,
                    3,
                    onThirdClick,
                    amountInput,
                    onAmountChange,
                    level,
                    difficulty,
                    false,
                    "testDrawingScore",
                    false,
                    helpText = "",
                    true,
                    "Use the interactive canvas to trace and memorize the correct stroke order for HSK characters. Perfect for developing \"muscle memory\" for writing.\nDifficulty level 3 will take away the outline, use it to write characters blindly"
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Exercises",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                )

                ExercicesCard(
                    "Character meaning",
                    expanded[0],
                    onCardClick,
                    0,
                    onFirstClick,
                    amountInput,
                    onAmountChange,
                    level,
                    difficulty,
                    true,
                    "hanZiMeaningScore",
                    true,
                    "pinyin",
                    true,
                    " Challenge your recognition skills. Choose the correct translation for the displayed HSK character to build a strong vocabulary foundation.\nDifficulty changes how many confusing answers appear"
                )

                ExercicesCard(
                    "Character pinyin",
                    expanded[1],
                    onCardClick,
                    1,
                    onSecondClick,
                    amountInput,
                    onAmountChange,
                    level,
                    difficulty,
                    true,
                    "hanZiPinYinScore",
                    true,
                    "translation",
                    true,
                    " Challenge your recognition skills. Choose the correct pinyin for the displayed HSK character to build a strong vocabulary foundation.\nDifficulty changes how many confusing answers appear"
                )

                ExercicesCard(
                    "Scrambled sentences",
                    expanded[2],
                    onCardClick,
                    2,
                    onFourthClick,
                    amountInput,
                    onAmountChange,
                    level,
                    difficulty,
                    true,
                    "sentencesScore",
                    true,
                    "pinyin",
                    true,
                    "Construct grammatically correct HSK sentences by reordering shuffled character blocks. The ultimate test of your understanding of Chinese syntax.\nDifficulty level changes amount of blocks that can genarate"
                )




            }
        }
    }
}
