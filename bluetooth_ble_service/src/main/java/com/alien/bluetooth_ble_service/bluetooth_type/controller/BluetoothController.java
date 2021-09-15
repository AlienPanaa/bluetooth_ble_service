package com.alien.bluetooth_ble_service.bluetooth_type.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.bluetooth_type.service.BluetoothService;
import com.alien.bluetooth_ble_service.basic_type.contoller.CommonController;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothCommonService;
import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;
import com.alien.bluetooth_ble_service.bluetooth_type.service.BluetoothServiceBinder;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothSetting;


public class BluetoothController extends CommonController<BluetoothSetting> {
    private static final String TAG = BluetoothController.class.getSimpleName();
    private static final BluetoothController instance = new BluetoothController();
    private static final int BLUETOOTH_CODE_IN_MAP = 0x10 << 2;

    private BluetoothSetting bluetoothSetting = BluetoothSetting.getDefaultSetting();

    private BluetoothController() {
        bluetoothInstanceMap.put(BluetoothProfile.HEADSET, null);
        bluetoothInstanceMap.put(BluetoothProfile.HEALTH, null);
    }

    public static BluetoothController getInstance() {
        return instance;
    }

    public BluetoothSetting getBluetoothSetting() {
        return bluetoothSetting;
    }

    public void setBluetoothSetting(@NonNull BluetoothSetting bluetoothSetting) {
        this.bluetoothSetting = bluetoothSetting;
    }

    @Override
    public boolean checkHardware(@NonNull Context context) {

        if(bluetoothSetting.getBluetoothAdapter() == null) {
            bluetoothSetting.getBluetoothErrorListener().onError(BluetoothErrorListener.DEVICE_LOSE_BLUETOOTH);
            return false;
        }

        return true;
    }

    @Override
    protected Class<? extends BluetoothCommonService> getServiceClass() {
        return BluetoothService.class;
    }

    @Override
    protected int getInstanceCode() {
        return BLUETOOTH_CODE_IN_MAP;
    }

    @Override
    public BluetoothServiceBinder getServiceBinder() throws Exception {
        return (BluetoothServiceBinder) super.getServiceBinder();
    }

    // --------------------------------------------------------------------------------------

    private BluetoothHeadset bluetoothHeadset;
    private BluetoothHealth bluetoothHealth;

    public void startHeadset(@NonNull Context context) {
        startProxy(context, BluetoothProfile.HEADSET);
    }

    public void stopHeadset() {
        closeProxy(BluetoothProfile.HEADSET);
    }

    public void startHealth(@NonNull Context context) {
        startProxy(context, BluetoothProfile.HEALTH);
    }

    public void stopHealth() {
        closeProxy(BluetoothProfile.HEALTH);
    }

    private void startProxy(@NonNull Context context, int profile) {
        BluetoothAdapter bluetoothAdapter = bluetoothSetting.getBluetoothAdapter();

        bluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                saveInstance(profile, proxy);
            }

            public void onServiceDisconnected(int profile) {
                removeInstance(profile);
            }
        }, profile);
    }

    private void closeProxy(int profile) {
        BluetoothAdapter bluetoothAdapter = bluetoothSetting.getBluetoothAdapter();

        bluetoothAdapter.closeProfileProxy(profile, bluetoothHeadset);
    }




}
