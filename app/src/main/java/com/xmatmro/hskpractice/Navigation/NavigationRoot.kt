package com.xmatmro.hskpractice.Navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
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
import com.xmatmro.hskpractice.Screens.SentencesScreen
import com.xmatmro.hskpractice.Screens.StudyScreen
import com.xmatmro.hskpractice.Screens.TestDrawingScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@OptIn(ExperimentalAnimationApi::class)
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
                    subclass(Route.TestDrawing::class,Route.TestDrawing.serializer())
                    subclass(Route.Sentences::class,Route.Sentences.serializer())
                    subclass(Route.Study::class,Route.Study.serializer())
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
        transitionSpec = {
            slideInHorizontally(initialOffsetX = {it}) togetherWith slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = { key ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key ) {
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
                            onFirstClick =  { level,amount,difficulty,checked ->
                                backStack.remove(key)
                                backStack.add(Route.HanZiMeaning(level,amount, difficulty,checked))



                            },
                            onSecondClick = { level,amount,difficulty,checked ->
                                backStack.remove(key)
                                backStack.add(Route.HanZiPinYin(level,amount, difficulty,checked))
                            },
                            onThirdClick = { level, amount,difficulty,checked ->
                                backStack.remove(key)
                                backStack.add(Route.TestDrawing(level,amount,difficulty))
                            },
                            onFourthClick = { level, amount,difficulty,checked ->
                                backStack.remove(key)
                                backStack.add(Route.Sentences(level,amount,difficulty,checked))
                            },
                            onFifthClick = {level,amount,difficulty,checked ->
                                backStack.remove(key)
                                backStack.add(Route.Study(level))
                            }
                        )
                    }
                }

                is Route.HanZiMeaning -> {
                    NavEntry(key) {
                        HanZiMeaningScreen(level = key.level, amount = key.amount,difficulty = key.difficulty, back = {
                            backStack.removeAt(backStack.lastIndex)
                            backStack.add(Route.Exercices(key.level))
                        }, checked = key.checked)
                    }
                }

                is Route.HanZiPinYin ->{
                    NavEntry(key) {
                        HanZiPinYinScreen(level = key.level, amount = key.amount,difficulty = key.difficulty, back = {
                            backStack.removeAt(backStack.lastIndex)
                            backStack.add(Route.Exercices(key.level))

                        }, checked = key.checked)
                    }

                }

                is Route.TestDrawing ->{
                    NavEntry(key) {
                        TestDrawingScreen(level = key.level, amount = key.amount, difficulty = key.difficulty, back = {
                            backStack.removeAt(backStack.lastIndex)
                            backStack.add(Route.Exercices(key.level))
                        })
                    }
                }

                is Route.Sentences -> {
                    NavEntry(key) {
                        SentencesScreen(level = key.level, amount = key.amount, difficulty = key.difficulty, checked = key.checked,back = {
                            backStack.removeAt(backStack.lastIndex)
                            backStack.add(Route.Exercices(key.level))
                        })

                    }
                }

                is Route.Study -> {
                    NavEntry(key) {
                        StudyScreen(level = key.level, back = {
                            backStack.removeAt(backStack.lastIndex)
                            backStack.add(Route.Exercices(key.level))
                        })
                    }
                }
                else -> error("Unknown route: $key")
            }
        }
    )
}
