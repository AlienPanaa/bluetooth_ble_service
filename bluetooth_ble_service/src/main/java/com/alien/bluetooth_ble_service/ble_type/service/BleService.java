package com.alien.bluetooth_ble_service.ble_type.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.alien.bluetooth_ble_service.basic_type.service.BluetoothCommonService;


public class BleService extends BluetoothCommonService {

    private static final String TAG = BleService.class.getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BleServiceBinder(this);
    }

}
