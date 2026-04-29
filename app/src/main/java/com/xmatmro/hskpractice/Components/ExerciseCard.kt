package com.xmatmro.hskpractice.Components

import android.R.attr.onClick
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xmatmro.hskpractice.ViewModels.GameViewModel
import com.xmatmro.hskpractice.ViewModels.Score

@Composable
fun ExercicesCard(
    title: String,
    expanded: Boolean,
    onCardClick: (Int) -> Unit,
    Index: Int,
    onButtonClick: (Int,Int, Int,Boolean) -> Unit,
    amountInput: String,
    onAmountChange: (String) -> Unit,
    level: Int,
    difficulty: Int,
    viewModelName: String,
    help: Boolean,
    helpText: String
){

    val cornerRadius by animateDpAsState(
        targetValue = if (expanded) 25.dp else 30.dp,
        label = "corner_radius_animation"
    )
    val context = LocalContext.current
    val gameViewModel: GameViewModel = viewModel(viewModelStoreOwner = context as ViewModelStoreOwner)
    val thisExerciseScore = gameViewModel.scores[viewModelName] ?: Score()
    var checked by remember { mutableStateOf(false) }


    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(cornerRadius),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(indication = null,
                interactionSource = null,
                onClick = {onCardClick(Index)})
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(thickness = 2.dp)
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Średni wynik:",
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${thisExerciseScore.average}%",
                        color =  if (thisExerciseScore.average < 40) Color.Red else if (thisExerciseScore.average > 80) Color.Green else MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                HorizontalDivider(thickness = 2.dp)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            onAmountChange(input)
                        }
                    },
                    label = { Text("Ilość zadań") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(50.dp)
                )
                if (help){
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text="Czy chcesz ${helpText}?"
                        )
                        Switch(
                            checked = checked,
                            onCheckedChange = {
                                checked = it
                            },
                            thumbContent = {
                                if(checked){
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            }

                        )
                    }
                }


                Button(
                    onClick = {
                        amountInput.toIntOrNull()?.let {
                            if(it <= 0){
                                onAmountChange("10")
                            }
                        }
                        val count = amountInput.toIntOrNull() ?: 10
                        onButtonClick(level, count, difficulty,checked)
                    },
                    modifier = Modifier,
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("Lecimy!", fontSize = 18.sp, modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}
