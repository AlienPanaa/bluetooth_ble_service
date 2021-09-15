package com.alien.bluetooth_ble_service.basic_type.setting;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;


public abstract class CommonSetting {

    public abstract BluetoothAdapter getDefaultBluetoothAdapter(@NonNull Context context);

    private final BluetoothAdapter bluetoothAdapter;

    public CommonSetting(CommonSetting.Builder builder) {
        this.bluetoothAdapter = builder.bluetoothAdapter;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public abstract static class Builder {

        private BluetoothAdapter bluetoothAdapter;

        public BluetoothAdapter getBluetoothAdapter() {
            return bluetoothAdapter;
        }
        public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
            this.bluetoothAdapter = bluetoothAdapter;
        }

        public abstract CommonSetting build();

    }

}
