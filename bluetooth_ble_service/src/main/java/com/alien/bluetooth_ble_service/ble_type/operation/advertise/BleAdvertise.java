package com.alien.bluetooth_ble_service.ble_type.operation.advertise;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.util.Log;

import com.alien.bluetooth_ble_service.ble_type.bean.AdvertiseInfo;

public class BleAdvertise extends AdvertiseCallback {
    private static final String TAG = BleAdvertise.class.getSimpleName();

    private BluetoothLeAdvertiser bluetoothLeAdvertiser;

    public void startAdvertise(BluetoothLeAdvertiser bluetoothLeAdvertiser, AdvertiseInfo advertiseInfo) {
        this.bluetoothLeAdvertiser = bluetoothLeAdvertiser;

        AdvertiseSettings advertiseSettings = advertiseInfo.getAdvertiseSettings();
        AdvertiseData advertiseData = advertiseInfo.getAdvertiseData();
        AdvertiseData scanResponse = advertiseInfo.getScanResponse();

        bluetoothLeAdvertiser.startAdvertising(advertiseSettings, advertiseData, scanResponse, this);

    }

    public boolean stopAdvertise() {
        if(bluetoothLeAdvertiser == null) {
            return false;
        }

        bluetoothLeAdvertiser.stopAdvertising(this);
        return true;
    }

    @Override
    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
        Log.i(TAG, "onStartSuccess: " + settingsInEffect.toString());

        // TODO:
    }

    @Override
    public void onStartFailure(int errorCode) {
        Log.i(TAG, "onStartFailure: " + errorCode);

        // TODO:
    }
}
