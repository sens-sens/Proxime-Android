package com.androsmith.proxime.domain.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Byte
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BLEManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE)
            as? BluetoothManager
        ?: throw Exception("Bluetooth not supported")


    private val scanner = bluetoothManager.adapter.bluetoothLeScanner

    private var scannedDevices: MutableSet<String?> = mutableSetOf()

    private val _devices = MutableStateFlow(listOf<BluetoothDevice>())

    val devices: StateFlow<List<BluetoothDevice>> = _devices

    private val scanCallback = object : ScanCallback() {


        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (result?.device?.address !in scannedDevices) {
                result?.device?.let {
                    _devices.value = _devices.value + it
                    Log.d("BLE", devices.value.toString())
                }
                scannedDevices.add(result?.device?.address)
                Log.d("BLE", result?.device?.address ?: "Unknown")
            }
        }

        override fun onScanFailed(errorCode: Int) {

            Log.e("BLE", "Bluetooth scan failed!")
            super.onScanFailed(errorCode)
        }
    }

    private var scanning = false

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 15000

    private val handler = Handler(Looper.getMainLooper())

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun scanDevices() {
        if (!scanning) {
            handler.postDelayed({
                scanning = false
                scanner.stopScan(scanCallback)
            }, SCAN_PERIOD)

            scanning = true
            scanner.startScan(scanCallback)
        } else {
            handler.removeCallbacksAndMessages(null) // stop any scheduled stop
            scanning = false
            scanner.stopScan(scanCallback)
        }
    }


    private var bluetoothGatt: BluetoothGatt? = null

    private var _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private var services: List<BluetoothGattService> = emptyList()



    val serviceUUID = UUID.fromString("65e34af1-d6f5-498e-a782-ada0cac2768d")
    val irCharacteristicUUID = UUID.fromString("c11c329c-ffda-4420-b581-7feab0e47775")
    val buttonCharacteristicUUID = UUID.fromString("2b089712-34da-4fc9-b692-ad3fe0a13e13")


    private var _irSensor = MutableStateFlow(false)
    val irSensor: StateFlow<Boolean> = _irSensor


    private var _button = MutableStateFlow(false)
    val button: StateFlow<Boolean> = _button



    private val callback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            if (status != BluetoothGatt.GATT_SUCCESS) {
                _isConnected.value = false
                return
            }

            if (newState == BluetoothGatt.STATE_CONNECTED) {
                _isConnected.value = true
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            services = gatt.services

            Log.d("BLE Services",services.toString())
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic
        ) {
            super.onCharacteristicChanged(gatt, characteristic)
            if (characteristic.uuid == irCharacteristicUUID){
                _irSensor.value = characteristic.value[0] == Byte.valueOf(1)
            }
            else if (characteristic.uuid == buttonCharacteristicUUID){
                _button.value = characteristic.value[0] == Byte.valueOf(1)
            }
            Log.v("BLE Sensor Value: ", characteristic.value.contentToString())
        }

    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(bluetoothDevice: BluetoothDevice) {
        bluetoothGatt = bluetoothDevice.connectGatt(context, false, callback)
    }





    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun discoverServices() {
        bluetoothGatt?.discoverServices()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun startReceivingIRSensorUpdates() {
        val service = bluetoothGatt?.getService(serviceUUID)

        val characteristic = service?.getCharacteristic(irCharacteristicUUID)
        if (characteristic != null) {
            bluetoothGatt?.setCharacteristicNotification(characteristic, true)

            val CLIENT_CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
            val desc = characteristic.getDescriptor(CLIENT_CONFIG_DESCRIPTOR)
            desc?.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            bluetoothGatt?.writeDescriptor(desc)
        } else{
            Log.d("BLE Notify", "Char not found")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun startReceivingButtonUpdates() {
        val service = bluetoothGatt?.getService(serviceUUID)

        val characteristic = service?.getCharacteristic(buttonCharacteristicUUID)
        if (characteristic != null) {
            bluetoothGatt?.setCharacteristicNotification(characteristic, true)

            val CLIENT_CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
            val desc = characteristic.getDescriptor(CLIENT_CONFIG_DESCRIPTOR)
            desc?.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
            bluetoothGatt?.writeDescriptor(desc)
        } else{
            Log.d("BLE Notify", "Char not found")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getSensorData() {
        startReceivingButtonUpdates()
        startReceivingIRSensorUpdates()
    }
}