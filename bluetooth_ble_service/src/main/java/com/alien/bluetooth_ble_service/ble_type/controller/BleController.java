package com.alien.bluetooth_ble_service.ble_type.controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.ble_type.service.BleService;
import com.alien.bluetooth_ble_service.basic_type.contoller.CommonController;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothCommonService;
import com.alien.bluetooth_ble_service.ble_type.service.BleServiceBinder;
import com.alien.bluetooth_ble_service.ble_type.setting.BleSetting;


public class BleController extends CommonController<BleSetting> {
    private static final String TAG = BleController.class.getSimpleName();
    private static final BleController instance = new BleController();
    private static final int BLE_CODE_IN_MAP = 0x10 << 3;

    private BleSetting bleSetting = BleSetting.getDefaultSetting();


    private BleController() { }

    public static BleController getInstance() {
        return instance;
    }

    public CommonController<BleSetting> setBluetoothSetting(BleSetting setting) {
        this.bleSetting = setting;

        return this;
    }

    @Override
    public BleSetting getBluetoothSetting() {
        return bleSetting;
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
        return BleService.class;
    }

    @Override
    protected int getInstanceCode() {
        return BLE_CODE_IN_MAP;
    }

    @Override
    public BleServiceBinder getServiceBinder() throws Exception {
        return (BleServiceBinder) super.getServiceBinder();
    }
}
