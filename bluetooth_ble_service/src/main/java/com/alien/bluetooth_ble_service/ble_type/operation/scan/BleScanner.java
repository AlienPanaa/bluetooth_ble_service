package com.alien.bluetooth_ble_service.ble_type.operation.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;


import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;
import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;
import com.alien.bluetooth_ble_service.ble_type.bean.ScanInfo;
import com.alien.bluetooth_ble_service.ble_type.service.GattController;
import com.alien.bluetooth_ble_service.ble_type.setting.BleScanSetting;

import java.util.List;

public class BleScanner extends BluetoothScan<BleScanSetting> {

    private static final String TAG = BleScanner.class.getSimpleName();

    @FunctionalInterface
    public interface ConnectAction {
        GattController onConnect(BluetoothDevice device);
    }

    private final ConnectAction connectAction;

    public BleScanner(@NonNull BluetoothAdapter bluetoothAdapter, @NonNull ConnectAction connectAction) {
        super(bluetoothAdapter);

        this.connectAction = connectAction;
    }

    private BleScanSetting scanSetting;
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(scanSetting == null) {
                return;
            }
            scanSetting.getScanResultListener().OnScanResult(result, scanSetting.getBluetoothDeviceListener());

            String connectAddress = scanSetting.getConnectAddress();
            BluetoothDevice device = result.getDevice();
            if(connectAddress == null || device == null) {
                return;
            }

            if(connectAddress.equals(device.getAddress()) && stopScan()) {
                scanSetting.getConnectedResult().onResult(connectAction.onConnect(device));
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            if(results == null || results.isEmpty() || scanSetting == null) {
                return;
            }
            scanSetting.getScanResultListener().onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            if(scanSetting == null) {
                return;
            }
            scanSetting.getScanResultListener().onScanFailed(errorCode);
        }
    };

    @Override
    protected void scanFinishAction(ScanSetting scanSetting) {
        scanSetting.getScanStateListener().onScanState(false);
    }

    @Override
    protected boolean startScanAction(BluetoothAdapter bluetoothAdapter, BleScanSetting scanSetting) {
        this.scanSetting = scanSetting;

        boolean result;

        BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        if(bluetoothLeScanner == null) {
            result = false;
        } else {
            result = true;

            ScanInfo scanInfo = scanSetting.getScanInfo();

            ScanSettings settings = scanInfo.getScanSettings();
            List<ScanFilter> filters = scanInfo.getScanFilters();

            bluetoothLeScanner.startScan(filters, settings, scanCallback);

            scanSetting.getScanStateListener().onScanState(true);
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
