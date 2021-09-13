package com.alien.bluetooth_ble_service.basic_type.setting;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;


public abstract class CommonSetting {
    private boolean ignoreSame = true;

    private BluetoothAdapter bluetoothAdapter;

    private ScanStateListener scanStateListener;
    private BluetoothDeviceListener bluetoothDeviceListener;

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public abstract BluetoothAdapter getDefaultBluetoothAdapter(@NonNull Context context);

    public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public ScanStateListener getScanStateListener() {
        return scanStateListener;
    }

    public CommonSetting setScanStateListener(ScanStateListener scanStateListener) {
        this.scanStateListener = scanStateListener;
        return this;
    }

    public BluetoothDeviceListener getBluetoothDeviceListener() {
        return bluetoothDeviceListener;
    }

    public CommonSetting setBluetoothDeviceListener(BluetoothDeviceListener bluetoothDeviceListener) {
        this.bluetoothDeviceListener = bluetoothDeviceListener;
        return this;
    }

    public boolean isIgnoreSame() {
        return ignoreSame;
    }

    public CommonSetting setIgnoreSame(boolean ignoreSame) {
        this.ignoreSame = ignoreSame;
        return this;
    }

}
