package com.xmatmro.hskpractice.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xmatmro.hskpractice.Components.SegmentedControl
import com.xmatmro.hskpractice.Components.SegmentedControlButton

@Composable
fun HomeScreen(
    onStartClick: (Int) -> Unit
){
    var level by rememberSaveable { mutableStateOf(1) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding(),
        ){
            //Miał być dropdown, ale z pasji postanowił crashować aplikację
            Text("Wybierz poziom HSK")
            SegmentedControl {
                listOf<Int>(1,2,3,4,5,6).forEach { hskLevel ->
                    SegmentedControlButton(
                        onClick = { level = hskLevel },
                        level = hskLevel,
                        selected = level == hskLevel
                    )
                }
            }
            Button(
                onClick = { onStartClick(level) },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Start")

            }
        }
    }


}


