package com.alien.bluetooth_ble_service.ble_type.operation.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;


import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;
import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;
import com.alien.bluetooth_ble_service.ble_type.controller.BleController;
import com.alien.bluetooth_ble_service.ble_type.listener.ScanResultListener;
import com.alien.bluetooth_ble_service.ble_type.setting.ScanInfo;

import java.util.List;

public class BLEScanAction extends BluetoothScan {

    private static final String TAG = BLEScanAction.class.getSimpleName();

    private final BluetoothDeviceListener bluetoothDeviceListener;
    private final ScanStateListener scanStateListener;
    private final ScanResultListener scanResultListener;

    public BLEScanAction(BluetoothAdapter bluetoothAdapter,
                         ScanResultListener scanResultListener,
                         BluetoothDeviceListener bluetoothDeviceListener, ScanStateListener scanStateListener) {
        super(bluetoothAdapter);

        this.bluetoothDeviceListener = bluetoothDeviceListener;
        this.scanResultListener = scanResultListener;
        this.scanStateListener = scanStateListener;
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            scanResultListener.OnScanResult(result, bluetoothDeviceListener);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            if(results == null || results.isEmpty()) {
                return;
            }
            scanResultListener.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            scanResultListener.onScanFailed(errorCode);
        }
    };

    @Override
    protected void scanFinishAction() {
        scanStateListener.onScanState(false);
    }

    @Override
    protected boolean startScanAction(BluetoothAdapter bluetoothAdapter) {
        boolean result;

        BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        if(bluetoothLeScanner == null) {
            result = false;
        } else {
            result = true;

            ScanInfo scanInfo = BleController.getInstance().getBluetoothSetting().getScanInfo();

            ScanSettings settings = scanInfo.getScanSettings();
            List<ScanFilter> filters = scanInfo.getScanFilters();

            bluetoothLeScanner.startScan(filters, settings, scanCallback);

            scanStateListener.onScanState(true);
        }

        return result;
    }

    @Override
    protected boolean stopScanAction(BluetoothAdapter bluetoothAdapter) {
        boolean result;

        BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        if(bluetoothLeScanner == null) {
            result = false;
        } else {
            result = true;

            bluetoothLeScanner.stopScan(scanCallback);
        }

        return result;
    }

}
