package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothSocket;

import com.alien.bluetooth_ble_service.bluetooth_type.operation.read_write.BluetoothReadWriteSocket;


public class ConnectionInfo {

    private final BluetoothSocket bluetoothSocket;
    private final BluetoothReadWriteSocket readWriteSocket;

    public ConnectionInfo(BluetoothSocket bluetoothSocket) {
        this.bluetoothSocket = bluetoothSocket;

        readWriteSocket = new BluetoothReadWriteSocket(bluetoothSocket);
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public BluetoothReadWriteSocket getReadWriteSocket() {
        return readWriteSocket;
    }

}
