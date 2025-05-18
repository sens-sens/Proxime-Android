package com.androsmith.proxime.ui.screens.dashboard.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.androsmith.proxime.data.model.ConnectionState
import com.androsmith.proxime.data.model.SensorData

@Composable
fun DashboardContent(
    sensorData: SensorData,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
    ) {

        ProximityContainer(
            isBlocked = sensorData.isProximityBlocked
        )

        SegmentDivider(
            ignoreTop = true
        )


        ButtonStateContainer(
            enabled = sensorData.isButtonPressed, modifier = Modifier.padding(horizontal = 40.dp)
        )

        SegmentDivider()

        SensorDataCard(
            connectionState = if (sensorData.connectionState == ConnectionState.Connected) "Connected"
            else "Disconnected",
            proximityValue = if (sensorData.isProximityBlocked) "Blocked" else "Unblocked",
            buttonValue = if (sensorData.isButtonPressed) "Pressed" else "Released",
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        SegmentDivider()
    }
}

@Preview
@Composable
private fun DashboardContentPreview() {
    val sensorData = SensorData.default()
    DashboardContent(
        sensorData
    )
}


@Composable
fun SegmentDivider(
    modifier: Modifier = Modifier,
    ignoreTop: Boolean = false,
) {
    Column {

        if (!ignoreTop)
        Spacer(modifier.height(40.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(40.dp))
    }
}