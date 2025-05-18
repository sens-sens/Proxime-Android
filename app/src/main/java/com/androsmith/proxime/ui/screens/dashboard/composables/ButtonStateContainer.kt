package com.androsmith.proxime.ui.screens.dashboard.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun ButtonStateContainer(
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            "Button state", fontSize = 28.sp
        )

        Switch(
            checked = enabled, onCheckedChange = {}, modifier = Modifier.scale(1.5F)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonStateContainerPreview() {
    ButtonStateContainer(
        enabled = true
    )
}