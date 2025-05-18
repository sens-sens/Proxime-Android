package com.androsmith.proxime.ui.screens.dashboard.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SensorDataCard(
    connectionState: String,
    proximityValue: String,
    buttonValue: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()

    ) {

        SensorDataRow(
            label = "Device",
            value = connectionState
        )
        SensorDataRow(
            label = "Proximity sensor",
            value = proximityValue
        )
        SensorDataRow(
            label = "Button",
            value = buttonValue
        )

    }
}

@Composable
private fun SensorDataRow(label: String, value: String) {


    val textColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7F)

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = label,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = ":",
            color = textColor,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = value,
            color = textColor,
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SensorDataCardPreview() {
    SensorDataCard(
        connectionState = "Connected", proximityValue = "Blocked", buttonValue = "Pressed"
    )
}