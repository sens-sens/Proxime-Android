package com.androsmith.proxime.data.bluetooth

import android.bluetooth.BluetoothDevice

interface BluetoothService {

    fun scanDevices()

    fun connect(device: BluetoothDevice)

    fun disconnect()

    fun startListening()

    fun dispose()

}