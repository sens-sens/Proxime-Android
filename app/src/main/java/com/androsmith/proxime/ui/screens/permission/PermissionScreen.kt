package com.androsmith.proxime.ui.screens.permission

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.androsmith.proxime.domain.permissions.Permissions.BLE_PERMISSIONS

@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var shouldRequestBluetooth by remember { mutableStateOf(false) }

    val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

    val bluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            onPermissionGranted()
        } else {
            shouldRequestBluetooth = true
        }


    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
        if (granted.values.all { it }) {
            // User has granted all permissions
            bluetoothLauncher.launch(bluetoothIntent)

        } else {
            // TODO: handle potential rejection in the usual way
        }
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(BLE_PERMISSIONS)

    }

    LaunchedEffect(shouldRequestBluetooth) {
        if (shouldRequestBluetooth) {
            shouldRequestBluetooth = false
            bluetoothLauncher.launch(bluetoothIntent)
        }
    }
}