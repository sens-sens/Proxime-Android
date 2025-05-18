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
import com.androsmith.proxime.domain.model.Resource
import com.androsmith.proxime.data.model.ConnectionState
import com.androsmith.proxime.data.model.SensorData
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
import java.lang.Byte
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Int
import kotlin.String
import kotlin.getValue
import kotlin.lazy


@Singleton
@SuppressLint("MissingPermission")
class SensorBluetoothService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter,
) : BluetoothService {


    private val _sensorDataState = MutableStateFlow<Resource<SensorData>>(Resource.Loading())
    val sensorDataState: StateFlow<Resource<SensorData>> = _sensorDataState.asStateFlow()


    private val _scanResultsState =
        MutableStateFlow<Resource<List<BluetoothDevice>>>(Resource.Loading())
    val scanResultsState: StateFlow<Resource<List<BluetoothDevice>>> =
        _scanResultsState.asStateFlow()


    val CCD_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb"


    val proximeServiceUUID = UUID.fromString("65e34af1-d6f5-498e-a782-ada0cac2768d")
    val proximitySensorCharacteristicUUID = UUID.fromString("c11c329c-ffda-4420-b581-7feab0e47775")
    val buttonPressCharacteristicUUID = UUID.fromString("2b089712-34da-4fc9-b692-ad3fe0a13e13")

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
                    val updatedDevices = existingDevices + result.device
                    Resource.Success(
                        data = updatedDevices
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

        coroutineScope.launch {
            _scanResultsState.emit(
                Resource.Loading()
            )
        }
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
                    gatt?.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    coroutineScope.launch {
                        _sensorDataState.emit(
                            Resource.Success(
                                data = SensorData(
                                    isProximityBlocked = false,
                                    isButtonPressed = false,
                                    connectionState = ConnectionState.Disconnected
                                )
                            )
                        )
                    }
                    gatt?.close()
                }
            } else {

                Log.d("BLE", "Gatt successful!")
                coroutineScope.launch {
                    _sensorDataState.emit(
                        Resource.Error(message = "Connection failed!")
                    )
                }
                gatt?.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            startListening()

        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            when (characteristic.uuid) {
                proximitySensorCharacteristicUUID -> _sensorDataState.update { currentState ->
                    val buttonPressedValue =
                        (currentState as? Resource.Success)?.data?.isButtonPressed ?: false
                    val proximitySensorValue = characteristic.value[0] == Byte.valueOf(1)
                    Resource.Success(
                        SensorData(
                            isProximityBlocked = proximitySensorValue,
                            isButtonPressed = buttonPressedValue,
                            connectionState = ConnectionState.Connected,
                        )
                    )
                }

                buttonPressCharacteristicUUID -> _sensorDataState.update { currentState ->
                    val buttonPressedValue = characteristic.value[0] == Byte.valueOf(1)
                    val proximitySensorValue =
                        (currentState as? Resource.Success)?.data?.isProximityBlocked ?: false

                    Resource.Success(
                        SensorData(
                            isProximityBlocked = proximitySensorValue,
                            isButtonPressed = buttonPressedValue,
                            connectionState = ConnectionState.Connected,
                        )
                    )
                }
            }
        }

    }

    override fun connect(device: BluetoothDevice) {
        Log.d("BLE", "Connecting to device $device")
        bluetoothGatt = device.connectGatt(context, false, callback)

    }

    override fun disconnect() {
        bluetoothGatt?.disconnect()
    }


    override fun startListening() {
        val service = bluetoothGatt?.getService(proximeServiceUUID)
        if (service == null) {
            Log.d("BLE", "Service not found")
            coroutineScope.launch {
                _sensorDataState.emit(
                    Resource.Error("Service not found!")
                )
            }
        } else {

            coroutineScope.launch {
                _sensorDataState.emit(
                    Resource.Success(
                        data = SensorData(
                            isProximityBlocked = false,
                            isButtonPressed = false,
                            connectionState = ConnectionState.Connected
                        )
                    )
                )
            }

            val buttonPressCharacteristic = service.getCharacteristic(buttonPressCharacteristicUUID)
            val proximitySensorCharacteristic =
                service.getCharacteristic(proximitySensorCharacteristicUUID)

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
        val CLIENT_CONFIG_DESCRIPTOR = UUID.fromString(CCD_DESCRIPTOR)
        val descriptor = characteristic.getDescriptor(CLIENT_CONFIG_DESCRIPTOR)
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
        bluetoothGatt?.writeDescriptor(descriptor)
    }


    override suspend fun dispose() {
        _sensorDataState.emit(
            Resource.Loading()
        )
        scanner.stopScan(scanCallback)
        bluetoothGatt?.close()
        coroutineScope.cancel()
    }

}