package com.alien.bluetooth_ble_service.ble_type.setting;

import android.util.Log;

import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;
import com.alien.bluetooth_ble_service.ble_type.bean.ScanInfo;
import com.alien.bluetooth_ble_service.ble_type.listener.ScanResultListener;

public class BleScanSetting extends ScanSetting {

    private static final String TAG = BleScanSetting.class.getSimpleName();

    private ScanInfo scanInfo = ScanInfo.getDefault();
    private ScanResultListener scanResultListener = result -> Log.i(TAG, result.toString());

    public ScanInfo getScanInfo() {
        return scanInfo;
    }

    public ScanSetting setScanInfo(ScanInfo scanInfo) {
        this.scanInfo = scanInfo;
        return this;
    }

    public ScanResultListener getScanResultListener() {
        return scanResultListener;
    }

    public ScanSetting setScanResultListener(ScanResultListener scanResultListener) {
        this.scanResultListener = scanResultListener;
        return this;
    }
}

