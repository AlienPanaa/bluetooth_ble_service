package com.alien.bluetooth_ble_service.basic_type.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;

import androidx.annotation.NonNull;
import androidx.annotation.Size;


import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;

import java.util.Set;

public abstract class BluetoothBinder extends Binder {

    @FunctionalInterface
    private interface MyExtra {
        void onMyExtra(Intent intent);
    }

    protected final Context context;
    protected final BluetoothAdapter bluetoothAdapter;

    private final BluetoothScan bluetoothScan;

    public BluetoothBinder(@NonNull Context context) {
        this.context = context;

        bluetoothAdapter = getBluetoothAdapter(context);
        this.bluetoothScan = getBluetoothScanner(bluetoothAdapter);
    }

    @NonNull
    public abstract BluetoothAdapter getBluetoothAdapter(@NonNull Context context);

    @NonNull
    public abstract BluetoothScan getBluetoothScanner(BluetoothAdapter bluetoothAdapter);

    public boolean searchDevice(long scanTime) {
        return bluetoothScan.startScan(scanTime);
    }

    public void requestDiscoverableDevice(@Size(min = 1L, max = 300L) int secondTime) {
        sendIntent((Intent intent) -> {
            intent.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, secondTime);
            intent.putExtra(BluetoothAdapter.EXTRA_SCAN_MODE, bluetoothAdapter.getScanMode());
        });
    }

    public void listenerScanModeChange() {
        sendIntent((Intent intent) -> intent.putExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
    }

    private void sendIntent(MyExtra item) {
        Intent intent = new Intent();

        item.onMyExtra(intent);

        context.startActivity(intent);
    }

    public abstract boolean serverConnectDevice();

    public abstract boolean serverCloseDevice();

    public abstract boolean clientConnectDevice(BluetoothDevice device);

    public abstract boolean clientCloseDevice();

}
