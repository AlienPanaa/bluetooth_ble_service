package com.alien.bluetooth_ble_service.bluetooth_type.operation.scan;

import android.bluetooth.BluetoothAdapter;

import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;
import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;


public class BluetoothScanner extends BluetoothScan<ScanSetting> {

    public BluetoothScanner(BluetoothAdapter bluetoothAdapter) {
        super(bluetoothAdapter);
    }

    @Override
    protected boolean startScanAction(BluetoothAdapter bluetoothAdapter, ScanSetting scanSetting) {
        return bluetoothAdapter.startDiscovery();
    }

    @Override
    protected boolean stopScanAction(BluetoothAdapter bluetoothAdapter, ScanSetting scanSetting) {
        return bluetoothAdapter.cancelDiscovery();
    }

}
