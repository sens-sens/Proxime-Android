package com.androsmith.proxime.ui.screens.device

import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androsmith.proxime.R
import com.androsmith.proxime.ui.screens.device.composables.DeviceAppBar
import com.androsmith.proxime.ui.screens.device.composables.NoDeviceAnimation
import com.androsmith.proxime.ui.screens.device.composables.ScanDevicesButton

@Composable
fun DeviceScreen(
    modifier: Modifier = Modifier,
    navigateToDashBoard: () -> Unit,
    viewModel: DeviceViewModel = hiltViewModel(),
) {

    val devices = viewModel.devices.collectAsState()

    Scaffold(
        topBar = { DeviceAppBar() },
        floatingActionButton = {
            ScanDevicesButton(
                onClick = viewModel::startScanning
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->


        if (devices.value.isEmpty()) {

            Box(
                modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                NoDeviceAnimation()
            }
        } else {
            DeviceList(
                onConnect = {
                    viewModel.onConnect(it)
                    navigateToDashBoard()
                },
                devices = devices.value, modifier = Modifier.padding(innerPadding)
            )
        }

    }
}


@Composable
fun DeviceList(
    onConnect: (BluetoothDevice) -> Unit,
    devices: List<BluetoothDevice>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {

        items(devices) { device ->

            DeviceTile(
                onConnect = onConnect, device = device
            )
        }

    }
}

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
            Spacer(Modifier.width(16.dp))
            Column(
            ) {
                Text(
                    device.name, fontSize = 17.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    device.address,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5F)
                )
            }
            Spacer(Modifier.weight(1F))

            IconButton(
                onClick = {
                    onConnect(device)
                }) {
                Icon(
                    painterResource(R.drawable.arrow_right),
                    contentDescription = "Connect",
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(24.dp)
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1F),
            modifier = Modifier.padding(horizontal = 12.dp)

        )
    }
}