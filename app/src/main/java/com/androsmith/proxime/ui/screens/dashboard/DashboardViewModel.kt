package com.androsmith.proxime.ui.screens.dashboard

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androsmith.proxime.domain.model.Resource
import com.androsmith.proxime.data.bluetooth.SensorBluetoothService
import com.androsmith.proxime.data.model.SensorData
import com.androsmith.proxime.domain.bluetooth.BLEManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val sensorBluetoothService: SensorBluetoothService
) : ViewModel() {

    val sensorDataState: StateFlow<Resource<SensorData>> =
        sensorBluetoothService.sensorDataState

    fun dispose(){
        viewModelScope.launch {
            sensorBluetoothService.dispose()
        }
    }
}
