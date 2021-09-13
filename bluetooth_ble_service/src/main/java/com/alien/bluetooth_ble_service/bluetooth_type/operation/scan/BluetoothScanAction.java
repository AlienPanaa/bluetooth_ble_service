package com.alien.bluetooth_ble_service.bluetooth_type.operation.scan;

import android.bluetooth.BluetoothAdapter;

import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;


public class BluetoothScanAction extends BluetoothScan {

    public BluetoothScanAction(BluetoothAdapter bluetoothAdapter) {
        super(bluetoothAdapter);
    }

    @Override
    protected boolean startScanAction(BluetoothAdapter bluetoothAdapter) {
        return bluetoothAdapter.startDiscovery();
    }

    @Override
    protected boolean stopScanAction(BluetoothAdapter bluetoothAdapter) {
        return bluetoothAdapter.cancelDiscovery();
    }
}
