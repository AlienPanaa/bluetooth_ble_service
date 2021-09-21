package com.alien.bluetooth_ble_service.ble_type.operation.advertise;

import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;

import com.alien.bluetooth_ble_service.ble_type.bean.AdvertiseInfo;
import com.alien.bluetooth_ble_service.ble_type.listener.AdvertiseResultListener;

public class BleAdvertise extends AdvertiseCallback {
    private BluetoothLeAdvertiser bluetoothLeAdvertiser;

    private AdvertiseResultListener advertiseResultListener;

    public void startAdvertise(BluetoothLeAdvertiser bluetoothLeAdvertiser, AdvertiseInfo advertiseInfo) {
        this.bluetoothLeAdvertiser = bluetoothLeAdvertiser;

        advertiseResultListener = advertiseInfo.getAdvertiseResultListener();

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
        if(advertiseResultListener != null) {
            advertiseResultListener.onAdvertiseResult(settingsInEffect);
        }
    }

    @Override
    public void onStartFailure(int errorCode) {
        if(advertiseResultListener != null) {
            advertiseResultListener.onAdvertiseFail(errorCode);
        }
    }
}
