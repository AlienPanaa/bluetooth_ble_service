package com.alien.bluetooth_ble_service.basic_type.setting;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;


public abstract class CommonSetting {

    public abstract BluetoothAdapter getDefaultBluetoothAdapter(@NonNull Context context);


    private final boolean ignoreSame;

    private final BluetoothAdapter bluetoothAdapter;
    private final ScanStateListener scanStateListener;
    private final BluetoothDeviceListener bluetoothDeviceListener;

    public CommonSetting(CommonSetting.Builder builder) {
        this.ignoreSame = builder.ignoreSame;
        this.bluetoothAdapter = builder.bluetoothAdapter;
        this.scanStateListener = builder.scanStateListener;
        this.bluetoothDeviceListener = builder.bluetoothDeviceListener;
    }

    public boolean isIgnoreSame() {
        return ignoreSame;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public ScanStateListener getScanStateListener() {
        return scanStateListener;
    }

    public BluetoothDeviceListener getBluetoothDeviceListener() {
        return bluetoothDeviceListener;
    }

    public abstract static class Builder {

        private boolean ignoreSame = true;

        private BluetoothAdapter bluetoothAdapter;

        private ScanStateListener scanStateListener;
        private BluetoothDeviceListener bluetoothDeviceListener;

        public BluetoothAdapter getBluetoothAdapter() {
            return bluetoothAdapter;
        }
        public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
            this.bluetoothAdapter = bluetoothAdapter;
        }

        public ScanStateListener getScanStateListener() {
            return scanStateListener;
        }

        public Builder setScanStateListener(ScanStateListener scanStateListener) {
            this.scanStateListener = scanStateListener;
            return this;
        }

        public BluetoothDeviceListener getBluetoothDeviceListener() {
            return bluetoothDeviceListener;
        }

        public Builder setBluetoothDeviceListener(BluetoothDeviceListener bluetoothDeviceListener) {
            this.bluetoothDeviceListener = bluetoothDeviceListener;
            return this;
        }

        public boolean isIgnoreSame() {
            return ignoreSame;
        }

        public Builder setIgnoreSame(boolean ignoreSame) {
            this.ignoreSame = ignoreSame;
            return this;
        }

    }

}
