package com.androsmith.proxime.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import com.androsmith.proxime.data.model.ConnectionState
import com.androsmith.proxime.data.model.SensorData
import com.androsmith.proxime.domain.model.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
@SuppressLint("MissingPermission")
class SensorBluetoothService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter,
) : BluetoothService {

    companion object {


        const val CCD_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb"


        val PROXIME_SERVICE_UUID = UUID.fromString("65e34af1-d6f5-498e-a782-ada0cac2768d")
        val PROXIMITY_SENSOR_CHARACTERISTICS_UUID =
            UUID.fromString("c11c329c-ffda-4420-b581-7feab0e47775")
        val BUTTON_PRESS_CHARACTERISTIC_UUID =
            UUID.fromString("2b089712-34da-4fc9-b692-ad3fe0a13e13")

    }


    private val _sensorDataState =
        MutableStateFlow<Resource<SensorData>>(Resource.Success(SensorData.default()))
    val sensorDataState: StateFlow<Resource<SensorData>> = _sensorDataState.asStateFlow()


    private val _scanResultsState =
        MutableStateFlow<Resource<List<BluetoothDevice>>>(Resource.Loading("Scanning devices"))
    val scanResultsState: StateFlow<Resource<List<BluetoothDevice>>> =
        _scanResultsState.asStateFlow()

    private val scanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private var bluetoothGatt: BluetoothGatt? = null

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)


    private val scanCallback = object : ScanCallback() {

        private val scannedDevices = mutableSetOf<String>()

        override fun onScanResult(callbackType: Int, result: ScanResult) {

            if (result.device.address !in scannedDevices) {

                Log.d("BLE", "Device found!")

                _scanResultsState.update { currentState ->
                    val existingDevices =
                        (currentState as? Resource.Success)?.data ?: emptyList<BluetoothDevice>()

                    Resource.Success(
                        data = existingDevices + result.device
                    )

                }
                scannedDevices.add(result.device.address)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BLE", "Bluetooth scan failed!")
        }
    }

    override fun scanDevices() {


        _scanResultsState.value = Resource.Loading("Scanning devices")

        scanner.startScan(scanCallback)

        coroutineScope.launch {
            delay(15000)
            scanner.stopScan(scanCallback)
        }
    }


    private val callback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {

            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE", "Gatt successful!")
                if (newState == BluetoothProfile.STATE_CONNECTED) {

                    _sensorDataState.value = Resource.Loading(message = "Discovering services")

                    gatt?.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {


                    _sensorDataState.value = Resource.Success(
                        data = SensorData(
                            isProximityBlocked = false,
                            isButtonPressed = false,
                            connectionState = ConnectionState.Disconnected
                        )
                    )

                    gatt?.close()
                }
            } else {

                _sensorDataState.value = Resource.Error(message = "Connection failed!")

                gatt?.close()
                bluetoothGatt = null
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            startListening()

        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {

            val currentSensorData =
                (_sensorDataState.value as? Resource.Success)?.data ?: SensorData.default()
                    .copy(connectionState = ConnectionState.Connected)


            val newSensorData = when (characteristic.uuid) {
                PROXIMITY_SENSOR_CHARACTERISTICS_UUID -> {

                    val proximitySensorValue = characteristic.value[0] == 1.toByte()
                    currentSensorData.copy(isProximityBlocked = proximitySensorValue)

                }

                BUTTON_PRESS_CHARACTERISTIC_UUID -> {

                    val buttonPressedValue = characteristic.value[0] == 1.toByte()
                    currentSensorData.copy(isButtonPressed = buttonPressedValue)
                }

                else -> null
            }

            newSensorData?.let {
                _sensorDataState.value = Resource.Success(it)
            }
        }

    }

    override fun connect(device: BluetoothDevice) {


        _sensorDataState.value = Resource.Loading(message = "Connecting to device")

        bluetoothGatt = device.connectGatt(context, false, callback)

    }

    override fun disconnect() {
        bluetoothGatt?.disconnect()
    }


    override fun startListening() {
        val service = bluetoothGatt?.getService(PROXIME_SERVICE_UUID)
        if (service == null) {
            Log.d("BLE", "Service not found")

            _sensorDataState.value =
                Resource.Error("Service not found!")


        } else {


            _sensorDataState.value =
                Resource.Success(
                    data = SensorData(
                        isProximityBlocked = false,
                        isButtonPressed = false,
                        connectionState = ConnectionState.Connected
                    )
                )


            val buttonPressCharacteristic =
                service.getCharacteristic(BUTTON_PRESS_CHARACTERISTIC_UUID)
            val proximitySensorCharacteristic =
                service.getCharacteristic(PROXIMITY_SENSOR_CHARACTERISTICS_UUID)

            enableNotification(buttonPressCharacteristic)
            enableNotification(proximitySensorCharacteristic)
        }
    }


    private fun enableNotification(
        characteristic: BluetoothGattCharacteristic,
    ) {
        bluetoothGatt?.setCharacteristicNotification(characteristic, true)
        writeDescription(characteristic)
    }

    private fun writeDescription(
        characteristic: BluetoothGattCharacteristic,
    ) {
        val CCD_UUID = UUID.fromString(CCD_DESCRIPTOR)
        val descriptor = characteristic.getDescriptor(CCD_UUID)
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        bluetoothGatt?.writeDescriptor(descriptor)
    }


    override fun dispose() {
        scanner.stopScan(scanCallback)
        bluetoothGatt?.close()
        coroutineScope.cancel()
    }

}