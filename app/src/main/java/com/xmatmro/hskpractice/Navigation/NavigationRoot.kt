package com.xmatmro.hskpractice.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.xmatmro.hskpractice.Screens.ExercicesScreen
import com.xmatmro.hskpractice.Screens.HanZiMeaningScreen
import com.xmatmro.hskpractice.Screens.HanZiPinYinScreen
import com.xmatmro.hskpractice.Screens.HomeScreen
import com.xmatmro.hskpractice.Screens.TestDrawingScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Composable
fun NavigationRoot(

){
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration{
            serializersModule = SerializersModule{
                polymorphic(NavKey::class){
                    subclass(Route.Home::class,Route.Home.serializer())
                    subclass(Route.Exercices::class,Route.Exercices.serializer())
                    subclass(Route.HanZiMeaning::class,Route.HanZiMeaning.serializer())
                    subclass(Route.HanZiPinYin::class,Route.HanZiPinYin.serializer())
                }

            }

        },
        Route.Home

    )
    NavDisplay(
        modifier = Modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator ()
        ),
        entryProvider = { key ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key) {
                        HomeScreen(
                            onStartClick = { level ->
                                backStack.add(Route.Exercices(level))
                            }
                        )
                    }

                }

                is Route.Exercices -> {
                    NavEntry(key) {
                        ExercicesScreen(
                            level = key.level,
                            onFirstClick =  { level,ammount,difficulty,checked ->
                                backStack.remove(key)
                                backStack.add(Route.HanZiMeaning(level,ammount, difficulty, back = {
                                    backStack.removeAt(backStack.lastIndex)
                                    backStack.add(Route.Exercices(level))
                                },checked))



                            },
                            onSecondClick = { level,ammount,difficulty,checked ->
                                backStack.remove(key)
                                backStack.add(Route.HanZiPinYin(level,ammount, difficulty, back = {
                                    backStack.removeAt(backStack.lastIndex)
                                    backStack.add(Route.Exercices(level))

                                },checked))
                            },
                            onThirdClick = { level ->
                                backStack.remove(key)
                                backStack.add(Route.TestDrawing(level, back = {
                                    backStack.removeAt(backStack.lastIndex)
                                    backStack.add(Route.Exercices(level))
                                }))
                            }
                        )
                    }
                }

                is Route.HanZiMeaning -> {
                    NavEntry(key) {
                        HanZiMeaningScreen(level = key.level, amount = key.amount,difficulty = key.difficulty, back = key.back, checked = key.checked)
                    }
                }

                is Route.HanZiPinYin ->{
                    NavEntry(key) {
                        HanZiPinYinScreen(level = key.level, amount = key.amount,difficulty = key.difficulty, back = key.back, checked = key.checked)
                    }

                }

                is Route.TestDrawing ->{
                    NavEntry(key) {
                        TestDrawingScreen(level = key.level, back = key.back)
                    }
                }
                else -> error("Unknown route: $key")
            }
        }
    )
}
