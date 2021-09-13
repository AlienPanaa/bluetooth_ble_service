package com.alien.bluetooth_ble_service.basic_type.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;


import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;

import java.util.HashSet;
import java.util.Set;

public abstract class BluetoothCommonService extends Service {
    private static final String TAG = BluetoothCommonService.class.getSimpleName();

    private final Set<String> deviceRecord = new HashSet<>();

    protected void notifyScanState(boolean isScanning) {
        CommonSetting commonSetting = getCommonSetting();

        if(!isScanning) {
            deviceRecord.clear();
        }

        ScanStateListener scanStateListener = commonSetting.getScanStateListener();
        if(scanStateListener != null) {
            scanStateListener.onScanState(isScanning);
        }
    }

    protected void notifyScanResult(BluetoothDevice device) {
        CommonSetting commonSetting = getCommonSetting();
        boolean ignoreSame = commonSetting.isIgnoreSame();

        if(ignoreSame) {
            boolean add = deviceRecord.add(device.getAddress());

            if(!add) {
                return;
            }
        }

        BluetoothDeviceListener bluetoothDeviceListener = commonSetting.getBluetoothDeviceListener();
        if(bluetoothDeviceListener == null) {
            return;
        }

        bluetoothDeviceListener.onBluetoothDevice(device);
    }

    protected abstract CommonSetting getCommonSetting();

}
