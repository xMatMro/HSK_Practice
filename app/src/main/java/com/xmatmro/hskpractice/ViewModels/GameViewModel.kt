package com.xmatmro.hskpractice.ViewModels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken

data class Score(
    val history: List<List<Int>> = listOf(),
    val totalAttempts: Int = 0,
    val totalPoints: Int = 0,
    val totalTasks: Int = 0
) {
//    val average: Int
//        get() = if (totalAttempts > 0 && totalTasks > 0) (totalPoints / totalTasks)*100 else 0
val average: Int get() {
        var tasks: Int = 0
        var points: Int = 0
        history.forEach { it ->
            tasks += it[1]
            points += it[0]
         }

        return if (tasks > 0 && totalAttempts > 0) {
             ((points.toDouble() / tasks)*100).toInt()
        } else {
            0
        }
}
}

class GameViewModel(application: Application): AndroidViewModel(application) {
    private val _scores = mutableStateMapOf<String, Score>()

    private val prefs = application.getSharedPreferences("hsk_practice_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    val scores: Map<String, Score> get() = _scores

    init {
        loadScores()
    }

    fun addPoints(exerciseKey: String, points: Int, tasks:Int) {
        val current = _scores[exerciseKey] ?: Score()

        val newHistory = (current.history + listOf(listOf(points,tasks))).takeLast(5)
        
        _scores[exerciseKey] = current.copy(
            history = newHistory,
            totalAttempts = current.totalAttempts + 1,
            totalPoints = current.totalPoints + points,
            totalTasks = current.totalTasks + tasks
        )
        saveScores()
    }
    var difficulty by mutableIntStateOf(prefs.getInt("saved_difficulty",1))
        private set
    var amount by mutableStateOf(prefs.getString("saved_amount","10")?:"10")
        private set
    var level by mutableIntStateOf(prefs.getInt("saved_level",1))
        private set
    fun loadScores(){
        val json = prefs.getString("scores_data", null)
        if (json != null) {
            val type = object: TypeToken<Map<String,Score>>(){}.type
            val savedScores: Map<String,Score> = gson.fromJson(json, type)
            _scores.putAll(savedScores)
        }
        else{
            _scores["hanZiMeaningScore"] = Score()
            _scores["hanZiPinYinScore"] = Score()
            _scores["sentenceScore"] = Score()
        }
    }

    fun updateLevel(level: Int){
        this.level = level
        prefs.edit {
            putInt("saved_level",level)
        }
    }

    fun updateSettings(difficulty: Int, amount: String){
        this.difficulty = difficulty
        this.amount = amount
        prefs.edit {
            putInt("saved_difficulty",difficulty)
            putString("saved_amount",amount)
        }
    }

    fun saveScores(){
        val json = gson.toJson(_scores.toMap())
        prefs.edit { putString("scores_data", json) }
    }
}
