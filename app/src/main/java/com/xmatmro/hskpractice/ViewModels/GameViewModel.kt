package com.xmatmro.hskpractice.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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

class GameViewModel: ViewModel() {
    private val _scores = mutableStateMapOf<String, Score>()
    val scores: Map<String, Score> get() = _scores

    init {
        _scores["hanZiMeaningScore"] = Score()
        _scores["hanZiPinYinScore"] = Score()
        _scores["sentenceScore"] = Score()
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
    }
}
