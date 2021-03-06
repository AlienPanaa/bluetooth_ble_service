package com.alien.bluetooth_ble_service.basic_type.contoller.bluetooth_info;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.exception.SdkUnSupportException;

import java.util.Set;

public class LocalBluetoothInfo {

    private final BluetoothAdapter adapter;

    public LocalBluetoothInfo(@NonNull BluetoothAdapter adapter) {
        this.adapter = adapter;

    }

    @SuppressLint("HardwareIds")
    public String getAddress() {
        return adapter.getAddress();
    }

    public int getScanMode() {
        return adapter.getScanMode();
    }

    public int getState() {
        return adapter.getState();
    }

    public String getName(){
        return adapter.getName();
    }

    public Set<BluetoothDevice> getBondedDevices() {
        return adapter.getBondedDevices();
    }

    public int getLeMaximumAdvertisingDataLength() throws SdkUnSupportException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return adapter.getLeMaximumAdvertisingDataLength();
        }

        throw new SdkUnSupportException(Build.VERSION_CODES.O);
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + getName() + "\n" +
                "Address: " + getAddress() + "\n" +
                "ScanMode: " + getScanMode() + "\n" +
                "BondedDevices: " + getBondedDevices();
    }

}

