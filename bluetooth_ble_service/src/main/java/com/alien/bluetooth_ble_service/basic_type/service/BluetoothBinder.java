package com.alien.bluetooth_ble_service.basic_type.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;

import androidx.annotation.NonNull;
import androidx.annotation.Size;


import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;
import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;

public abstract class BluetoothBinder extends Binder {

    protected final Context context;
    protected final BluetoothAdapter bluetoothAdapter;

    public BluetoothBinder(@NonNull Context context) {
        this.context = context;

        bluetoothAdapter = getBluetoothAdapter(context);
    }

    @NonNull
    public abstract BluetoothAdapter getBluetoothAdapter(@NonNull Context context);

}
