package com.androsmith.proxime.ui.screens.device.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.androsmith.proxime.R

@Composable
fun ScanDevicesButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painterResource(R.drawable.scan),
            contentDescription = "Scan Devices",
            modifier = Modifier.size(28.dp)
        )
    }
}