package com.xmatmro.hskpractice.Components

import android.content.Context
import com.xmatmro.hskpractice.HSKCharacters.HSKCharactersClass
import kotlinx.serialization.json.Json

private val hskJson = Json { ignoreUnknownKeys = true }

fun loadHSKData(context: Context, level: Int): List<HSKCharactersClass> {
    return try {
        val fileName = "HSK_$level.json"
        val inputStream = context.assets.open(fileName)
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        hskJson.decodeFromString<List<HSKCharactersClass>>(jsonString)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}