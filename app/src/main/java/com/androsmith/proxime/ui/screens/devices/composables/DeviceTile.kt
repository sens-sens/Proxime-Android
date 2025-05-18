package com.androsmith.proxime.ui.screens.devices.composables

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androsmith.proxime.R


@SuppressLint("MissingPermission")
@Composable
fun DeviceTile(
    device: BluetoothDevice, onConnect: (BluetoothDevice) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .padding(
                    top = 20.dp, bottom = 12.dp
                )
                .padding(horizontal = 12.dp)
        ) {

            BluetoothIcon()

            Spacer(Modifier.width(16.dp))

            Column(
            ) {
                Text(
                    device.name,
                    fontSize = 17.sp,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    device.address,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5F)
                )
            }
            Spacer(Modifier.weight(1F))
            ConnectDeviceButton(
                onConnect = { onConnect(device) },
            )
        }
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1F),
            modifier = Modifier.padding(horizontal = 12.dp)

        )
    }
}


@Composable
fun BluetoothIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(12.dp)
    ) {
        Icon(
            painterResource(R.drawable.bluetooth_thin),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ConnectDeviceButton(
    onConnect: () -> Unit,
    modifier: Modifier = Modifier,
) {

    IconButton(
        onClick = onConnect
    ) {
        Icon(
            painterResource(R.drawable.arrow_right),
            contentDescription = "Connect",
            modifier = Modifier
                .padding(end = 6.dp)
                .size(24.dp)
        )
    }
}