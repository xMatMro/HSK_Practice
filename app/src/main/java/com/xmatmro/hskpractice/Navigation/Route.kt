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
    data class HanZiMeaning(val level: Int, val ammount: Int,val difficulty:Int) : Route, NavKey


}