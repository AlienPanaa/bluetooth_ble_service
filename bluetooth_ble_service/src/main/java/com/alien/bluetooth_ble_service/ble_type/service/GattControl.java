package com.alien.bluetooth_ble_service.ble_type.service;

import android.bluetooth.BluetoothGatt;

import androidx.annotation.NonNull;

public class GattControl {

    private final BluetoothGatt bluetoothGatt;

    public GattControl(@NonNull BluetoothGatt bluetoothGatt) {
        this.bluetoothGatt = bluetoothGatt;
    }

    public boolean readRssi() {
        return bluetoothGatt.readRemoteRssi();
    }

    public boolean executeReliableWrite() {
        return bluetoothGatt.executeReliableWrite();
    }

    public boolean discoverServices() {
        return bluetoothGatt.discoverServices();
    }



}
