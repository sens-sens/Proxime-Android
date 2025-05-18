package com.androsmith.proxime.ui.screens.devices

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.androsmith.proxime.domain.model.Resource
import com.androsmith.proxime.data.bluetooth.SensorBluetoothService
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class DevicesViewModel @Inject constructor(
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

    override fun onCleared() {
        super.onCleared()
        sensorBluetoothService.dispose()
    }

}