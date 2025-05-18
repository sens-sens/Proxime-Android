package com.androsmith.proxime.ui.screens.device

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import com.androsmith.proxime.domain.model.Resource
import com.androsmith.proxime.data.bluetooth.SensorBluetoothService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val sensorBluetoothService: SensorBluetoothService
) : ViewModel() {

    init {
        startScanning()
    }

    val scanResultsState: StateFlow<Resource<List<BluetoothDevice>>> =
        sensorBluetoothService.scanResultsState


    fun startScanning() {
        sensorBluetoothService.scanDevices()
    }

    fun onConnect(device: BluetoothDevice) {
        sensorBluetoothService.connect(device)
    }

}