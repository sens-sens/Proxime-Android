package com.androsmith.proxime.ui.screens.devices

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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


