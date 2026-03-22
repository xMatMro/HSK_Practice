package com.xmatmro.hskpractice.Components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExercicesCard(
    title: String,
    expanded: Boolean,
    onCardClick: (Int) -> Unit,
    Index: Int,
    onButtonClick: (Int,Int,Int) -> Unit,
    amountInput: String,
    onAmountChange: (String) -> Unit,
    level: Int,
    difficulty: Int,
    onDifficultyChange: (Int) -> Unit
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = if (expanded) RoundedCornerShape(10) else RoundedCornerShape(50)
    ) {
        Column(
            modifier = Modifier
                .clickable { onCardClick(Index) }
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$title",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Poziom trudności",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                SegmentedControl {
                    listOf(1, 2, 3).forEach { difficultyLevel ->
                        SegmentedControlButton(
                            onClick = { onDifficultyChange(difficultyLevel) },
                            text = difficultyLevel.toString(),
                            selected = difficulty == difficultyLevel
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                    shape = RoundedCornerShape(50)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        amountInput.toIntOrNull()?.let {
                            if(it <= 0){
                                onAmountChange("10")
                            }
                        }
                        val count = amountInput.toIntOrNull() ?: 10
                        onButtonClick(level, count, difficulty)
                    },
                    modifier = Modifier,
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Lecimy!", fontSize = 18.sp, modifier = Modifier.padding(4.dp))
                }
            }
        }
    }
}