package com.androsmith.proxime.ui.screens.permission

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androsmith.proxime.R
import com.androsmith.proxime.domain.permissions.Permissions.BLE_PERMISSIONS

@Composable
fun PermissionScreen(
    onPermissionGranted: () -> Unit, modifier: Modifier = Modifier
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
            bluetoothLauncher.launch(bluetoothIntent)
        }
    }
    LaunchedEffect(Unit) {
        permissionLauncher.launch(BLE_PERMISSIONS)

    }

    if (shouldRequestBluetooth) PermissionContent(
        onClick = { bluetoothLauncher.launch(bluetoothIntent) },
    )


}

@Composable
fun PermissionContent(
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(R.drawable.bluetooth_slash),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F),
            contentDescription = stringResource(R.string.bluetooth_not_available),
            modifier = Modifier.size(160.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Proxime requires Bluetooth",
            fontSize = 16.sp,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onClick

        ) {
            Text(stringResource(R.string.turn_on))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionContentPreview() {
   PermissionContent(
       onClick = {}
   )
}