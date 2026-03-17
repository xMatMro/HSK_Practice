package com.xmatmro.hskpractice.Screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import kotlinx.serialization.json.Json

@Composable
fun ExercicesScreen(
    level: Int
){
    val context = LocalContext.current
    var charactersList by remember { mutableStateOf<List<HSKCharactersClass>>(emptyList()) }

    LaunchedEffect(level) {
        charactersList = loadHSKData(context, level)
    }

    Surface(color= MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
        ){
            Text("Level: $level")
            Text("Loaded: ${charactersList.size} characters")

            charactersList.firstOrNull()?.let {
                Text("First: ${it.hanzi} (${it.pinyin})")
            }
        }
    }
}

fun loadHSKData(context: Context, level: Int): List<HSKCharactersClass> {
    return try {
        val fileName = "HSK_$level.json"
        val inputStream = context.assets.open(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        Json.decodeFromString<List<HSKCharactersClass>>(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
