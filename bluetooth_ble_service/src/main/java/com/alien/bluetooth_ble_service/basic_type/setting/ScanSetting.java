package com.alien.bluetooth_ble_service.basic_type.setting;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;

public class ScanSetting {
    private static final String TAG = ScanSetting.class.getSimpleName();

    private boolean isIgnoreSame = true;
    private long scanTime = 12_000;
    private BluetoothDeviceListener bluetoothDeviceListener = device -> Log.i(TAG, device.toString());
    private ScanStateListener scanStateListener = isScanning -> Log.i(TAG, "Is scanning: " + isScanning);


    public boolean isIgnoreSame() {
        return isIgnoreSame;
    }

    public ScanSetting setIgnoreSame(boolean ignoreSame) {
        isIgnoreSame = ignoreSame;
        return this;
    }

    @NonNull
    public BluetoothDeviceListener getBluetoothDeviceListener() {
        return bluetoothDeviceListener;
    }

    public ScanSetting setBluetoothDeviceListener(BluetoothDeviceListener bluetoothDeviceListener) {
        this.bluetoothDeviceListener = bluetoothDeviceListener;
        return this;
    }

    @NonNull
    public ScanStateListener getScanStateListener() {
        return scanStateListener;
    }

    public ScanSetting setScanStateListener(ScanStateListener scanStateListener) {
        this.scanStateListener = scanStateListener;
        return this;
    }

    public long getScanTime() {
        return scanTime;
    }

    public void setScanTime(long scanTime) {
        this.scanTime = scanTime;
    }
}