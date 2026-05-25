package com.xmatmro.hskpractice.Navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {

    @Serializable
    data object Home: Route, NavKey

    @Serializable
    data class Exercices(val level: Int) : Route, NavKey

    @Serializable
    data class HanZiMeaning(val level: Int, val amount: Int,val difficulty:Int,val checked: Boolean) : Route, NavKey

    @Serializable
    data class HanZiPinYin(val level: Int, val amount: Int,val difficulty:Int,val checked: Boolean) : Route, NavKey

    @Serializable

    data class TestDrawing(val level: Int,val amount: Int,val difficulty: Int) : Route, NavKey

    @Serializable
    data class Sentences(val level: Int,val amount: Int,val difficulty: Int,val checked: Boolean) : Route, NavKey
    @Serializable
    data class  Study(val level: Int) : Route, NavKey
}

