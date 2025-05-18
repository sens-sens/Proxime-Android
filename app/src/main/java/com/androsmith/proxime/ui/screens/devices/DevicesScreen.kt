package com.androsmith.proxime.ui.screens.devices

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.androsmith.proxime.R
import com.androsmith.proxime.domain.model.Resource
import com.androsmith.proxime.ui.composables.ErrorContainer
import com.androsmith.proxime.ui.screens.devices.composables.DeviceAppBar
import com.androsmith.proxime.ui.screens.devices.composables.DeviceList
import com.androsmith.proxime.ui.screens.devices.composables.NoDeviceAnimation
import com.androsmith.proxime.ui.screens.devices.composables.ScanDevicesButton

@Composable
fun DeviceScreen(
    modifier: Modifier = Modifier,
    navigateToDashBoard: () -> Unit,
    viewModel: DevicesViewModel = hiltViewModel(),
) {


    val scanResults by viewModel.scanResultsState.collectAsStateWithLifecycle()


    Scaffold(
        topBar = { DeviceAppBar() },
        floatingActionButton = {
            ScanDevicesButton(
                onClick = viewModel::startScanning
            )
        },
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->

        when (scanResults) {
            is Resource.Loading -> NoDeviceAnimation(
                modifier = Modifier.fillMaxSize()
            )

            is Resource.Error -> ErrorContainer()

            is Resource.Success -> {

                val devices = scanResults.data ?: emptyList()

                DeviceList(
                    onConnect = {
                        viewModel.onConnect(it)
                        navigateToDashBoard()
                    },
                    devices = devices,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}


