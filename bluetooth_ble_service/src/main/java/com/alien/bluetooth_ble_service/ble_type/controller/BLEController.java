package com.alien.bluetooth_ble_service.ble_type.controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.ble_type.service.BLEService;
import com.alien.bluetooth_ble_service.basic_type.contoller.CommonController;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothCommonService;
import com.alien.bluetooth_ble_service.ble_type.service.BLEServiceBinder;


public class BLEController extends CommonController<BLESetting.Builder> {
    private static final String TAG = BLEController.class.getSimpleName();
    private static final BLEController instance = new BLEController();
    private static final int BLE_CODE_IN_MAP = 0x10 << 3;

    private BLESetting bleSetting = BLESetting.getDefaultSetting();


    private BLEController() { }

    public static BLEController getInstance() {
        return instance;
    }

    public CommonController<BLESetting.Builder> setBluetoothSetting(BLESetting setting) {
        this.bleSetting = setting;

        return this;
    }

    @Override
    public BLESetting.Builder getBluetoothSetting() {
        return bleSetting.getSetting();
    }

    @Override
    public boolean checkHardware(@NonNull Context context) {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.w(TAG, "Device lose BLE Service.");
            return false;
        }
        return true;
    }

    @Override
    protected Class<? extends BluetoothCommonService> getServiceClass() {
        return BLEService.class;
    }

    @Override
    protected int getInstanceCode() {
        return BLE_CODE_IN_MAP;
    }

    @Override
    public BLEServiceBinder getServiceBinder() throws Exception {
        return (BLEServiceBinder) super.getServiceBinder();
    }
}
