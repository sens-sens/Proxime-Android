package com.androsmith.proxime.ui.screens.dashboard

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.androsmith.proxime.domain.bluetooth.BLEManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bleManager: BLEManager
) : ViewModel() {

    val isConnected = bleManager.isConnected


    val ir = bleManager.irSensor
    val button = bleManager.button

    fun discover() {
        bleManager.discoverServices()
    }

    fun startNotify() {
        bleManager.getSensorData()
    }



}
