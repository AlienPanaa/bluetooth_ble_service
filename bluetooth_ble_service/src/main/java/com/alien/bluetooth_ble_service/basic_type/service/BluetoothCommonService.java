package com.alien.bluetooth_ble_service.basic_type.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;


import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;
import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;

import java.util.HashSet;
import java.util.Set;

public abstract class BluetoothCommonService extends Service {

    private static final String TAG = BluetoothCommonService.class.getSimpleName();


}
