package com.alien.bluetooth_ble_service.basic_type.setting;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.listener.Result;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;

import java.util.UUID;
import java.util.function.Consumer;

public class ScanSetting {
    private static final String TAG = ScanSetting.class.getSimpleName();

    private boolean isIgnoreSame = true;
    private long scanTime = 12_000;
    private String connectAddress;
    private Result<Boolean> connectedResult;

    private BluetoothDeviceListener bluetoothDeviceListener = device -> Log.i(TAG, device.toString());
    private ScanStateListener scanStateListener = isScanning -> Log.i(TAG, "Is scanning: " + isScanning);


    public boolean isIgnoreSame() {
        return isIgnoreSame;
    }

    public ScanSetting setIgnoreSame(boolean ignoreSame) {
        isIgnoreSame = ignoreSame;
        return this;
    }

    public BluetoothDeviceListener getBluetoothDeviceListener() {
        return bluetoothDeviceListener;
    }

    public ScanSetting setBluetoothDeviceListener(BluetoothDeviceListener bluetoothDeviceListener) {
        this.bluetoothDeviceListener = bluetoothDeviceListener;
        return this;
    }

    public ScanStateListener getScanStateListener() {
        return scanStateListener;
    }

    public ScanSetting setScanStateListener(ScanStateListener scanStateListener) {
        this.scanStateListener = scanStateListener;
        return this;
    }

    public String getConnectAddress() {
        return connectAddress;
    }

    public ScanSetting setConnectAddress(@NonNull String connectAddress, @NonNull Result<Boolean> connectedResult) {
        this.connectAddress = connectAddress;
        this.connectedResult = connectedResult;
        return this;
    }

    public Result<Boolean> getConnectedResult() {
        return connectedResult;
    }

    public long getScanTime() {
        return scanTime;
    }

    public void setScanTime(long scanTime) {
        this.scanTime = scanTime;
    }
}