package com.xmatmro.hskpractice.Components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedControl(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        shape = CircleShape,
    ) {
        LookaheadScope {
            Layout(
                content = {
                    @OptIn(ExperimentalSharedTransitionApi::class)
                    SelectedBackground(Modifier.animateBounds(this))

                    content()
                },
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .selectableGroup(),
            ) { measurables, constraints ->
                require(measurables.count { it.layoutId == SelectedButtonId } <= 1) {
                    "Segmented control must have at most one selected button"
                }

                val buttonMeasurables = measurables.filter { it.layoutId != SelectedBackgroundId }
                val buttonWidth = constraints.maxWidth / buttonMeasurables.size
                val buttonConstraints = constraints.copy(minWidth = buttonWidth, maxWidth = buttonWidth)
                val buttonPlaceables = buttonMeasurables.map { it.measure(buttonConstraints) }

                val selectedButtonIndex = buttonMeasurables.indexOfFirst { it.layoutId == SelectedButtonId }
                val selectedBackgroundMeasurable = measurables.first { it.layoutId == SelectedBackgroundId }
                val selectedBackgroundPlaceable = if (selectedButtonIndex >= 0) {
                    selectedBackgroundMeasurable.measure(buttonConstraints)
                } else {
                    null
                }

                layout(
                    width = buttonPlaceables.sumOf { it.width },
                    height = buttonPlaceables.maxOf { it.height },
                ) {
                    selectedBackgroundPlaceable?.placeRelative(x = selectedButtonIndex * buttonWidth, y = 0)

                    buttonPlaceables.forEachIndexed { index, it ->
                        it.placeRelative(x = index * buttonWidth, y = 0)
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentedControlButton(
    onClick: () -> Unit,
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .then(if (selected) Modifier.layoutId(SelectedButtonId) else Modifier)
            .widthIn(min = 80.dp)
            .heightIn(48.dp)
            .clip(CircleShape)
            .selectable(selected = selected, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = text,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun SelectedBackground(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.layoutId(SelectedBackgroundId),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = CircleShape,
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
    ) {}
}

const val SelectedButtonId = "SelectedButtonId"
const val SelectedBackgroundId = "SelectedBackgroundId"
