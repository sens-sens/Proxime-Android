package com.androsmith.proxime.ui.screens.devices.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
            contentDescription = stringResource(R.string.scan_devices),
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScanDevicesButtonPreview() {
    ScanDevicesButton(
        onClick = {}
    )
}