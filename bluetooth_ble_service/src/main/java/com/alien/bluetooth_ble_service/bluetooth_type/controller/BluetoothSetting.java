package com.alien.bluetooth_ble_service.bluetooth_type.controller;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;


import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.info_bean.BluetoothClientInfo;
import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;
import com.alien.bluetooth_ble_service.bluetooth_type.info_bean.BluetoothServerInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;


public class BluetoothSetting {
    private static final String TAG = BluetoothSetting.class.getSimpleName();

    @IntDef(
            {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE,
                    BluetoothAdapter.SCAN_MODE_NONE,
            }
    )
    @Retention(RetentionPolicy.SOURCE)
    @interface ScanMode {}

    private final Builder builder;

    BluetoothSetting(@NonNull Builder builder) {
        this.builder = builder;
    }

    public Builder getSetting() {
        return builder;
    }

    public static BluetoothSetting getDefaultSetting() {
        return new Builder().build();
    }

    public static final class Builder extends CommonSetting {
        private BluetoothServerInfo serverInfo;
        private BluetoothClientInfo clientInfo;
        private UUID uuid;

        private BluetoothErrorListener bluetoothErrorListener;

        private int scanMode = BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE;

        public Builder setErrorListener(BluetoothErrorListener bluetoothErrorListener) {
            this.bluetoothErrorListener = bluetoothErrorListener;
            return this;
        }

        public BluetoothErrorListener getErrorListener() {
            return bluetoothErrorListener;
        }

        public void setScanMode(@ScanMode int scanMode) {
            this.scanMode = scanMode;
        }

        public int getScanMode() {
            return scanMode;
        }

        public UUID getUuid() {
            return uuid == null ? UUID.randomUUID() : uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public BluetoothServerInfo getServerInfo() {
            return serverInfo;
        }

        public void setServerInfo(BluetoothServerInfo serverInfo) {
            this.serverInfo = serverInfo;
        }

        public BluetoothClientInfo getClientInfo() {
            return clientInfo;
        }

        public void setClientInfo(BluetoothClientInfo clientInfo) {
            this.clientInfo = clientInfo;
        }

        @Override
        public BluetoothAdapter getDefaultBluetoothAdapter(@NonNull Context context) {
            return BluetoothAdapter.getDefaultAdapter();
        }

        public BluetoothSetting build() {
            if(bluetoothErrorListener == null) {
                bluetoothErrorListener = (errorCode, e) -> Log.e(TAG, "BluetoothController error: " + errorCode);
            }

            if(serverInfo == null) {
                serverInfo = new BluetoothServerInfo();
            }

            if(getBluetoothAdapter() == null) {
                setBluetoothAdapter(BluetoothAdapter.getDefaultAdapter());
            }

            return new BluetoothSetting(this);
        }

    }

}
