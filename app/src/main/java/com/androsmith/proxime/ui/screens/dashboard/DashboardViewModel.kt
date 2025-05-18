package com.androsmith.proxime.ui.screens.dashboard

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.androsmith.proxime.data.bluetooth.SensorBluetoothService
import com.androsmith.proxime.data.model.SensorData
import com.androsmith.proxime.domain.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("MissingPermission")
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sensorBluetoothService: SensorBluetoothService
) : ViewModel() {

    val sensorDataState: StateFlow<Resource<SensorData>> = sensorBluetoothService.sensorDataState

    override fun onCleared() {
        super.onCleared()
        sensorBluetoothService.dispose()
    }
}
