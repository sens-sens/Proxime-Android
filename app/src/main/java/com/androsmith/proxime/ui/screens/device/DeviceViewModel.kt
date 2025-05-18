package com.androsmith.proxime.ui.screens.device

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.androsmith.proxime.domain.bluetooth.BLEManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val bleManager: BLEManager
) : ViewModel() {

    init {
        startScanning()
    }

    val devices = bleManager.devices



    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScanning() {
        bleManager.scanDevices()
    }

    fun onConnect(device: BluetoothDevice) {
        bleManager.connect(device)
    }

}