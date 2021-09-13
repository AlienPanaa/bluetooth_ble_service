package com.alien.bluetooth_ble_service.ble_type.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;
import com.alien.bluetooth_ble_service.ble_type.listener.BLEErrorListener;
import com.alien.bluetooth_ble_service.ble_type.listener.ScanResultListener;

import java.util.List;

public class BLESetting {
    private static final String TAG = BLESetting.class.getSimpleName();

    private final Builder builder;

    BLESetting(@NonNull Builder builder) {
        this.builder = builder;
    }

    public Builder getSetting() {
        return builder;
    }

    public static BLESetting getDefaultSetting() {
        return new Builder().build();
    }

    public static final class Builder extends CommonSetting {
        private BLEErrorListener BLEErrorListener;
        private ScanResultListener scanResultListener = result -> {
            Log.i(TAG, result.toString());
        };

        private List<ScanFilter> filters;
        private ScanSettings settings;

        private boolean autoConnect;

        public Builder setErrorListener(BLEErrorListener BLEErrorListener) {
            this.BLEErrorListener = BLEErrorListener;
            return this;
        }

        public ScanResultListener getScanResultListener() {
            return scanResultListener;
        }

        public void setScanResultListener(ScanResultListener scanResultListener) {
            this.scanResultListener = scanResultListener;
        }

        public BLEErrorListener getErrorListener() {
            return BLEErrorListener;
        }

        public List<ScanFilter> getFilters() {
            return filters;
        }

        public void setFilters(List<ScanFilter> filters) {
            this.filters = filters;
        }

        public ScanSettings getSettings() {
            return settings;
        }

        public void setSettings(ScanSettings settings) {
            this.settings = settings;
        }

        public boolean isAutoConnect() {
            return autoConnect;
        }

        public void setAutoConnect(boolean autoConnect) {
            this.autoConnect = autoConnect;
        }

        @Override
        public BluetoothAdapter getDefaultBluetoothAdapter(@NonNull Context context) {
            BluetoothManager bluetoothManager =
                    (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            return bluetoothManager.getAdapter();
        }

        public BLESetting build() {
            if(BLEErrorListener == null) {
                BLEErrorListener = (errorCode, e) -> Log.e(TAG, "BluetoothController error: " + errorCode);
            }

            if(scanResultListener == null) {
                scanResultListener = result -> Log.i(TAG, result.toString());
            }

            if(getBluetoothDeviceListener() == null) {
                setScanResultListener(bluetoothDevice -> Log.i(TAG, "bluetoothDevice info: " + bluetoothDevice));
            }

            if(getScanStateListener() == null) {
                setScanStateListener(isScanning -> Log.i(TAG, "Bluetooth is scanning: " + isScanning));
            }

            if(settings == null) {
                settings = new ScanSettings.Builder().build();
            }

            return new BLESetting(this);
        }

    }

}
