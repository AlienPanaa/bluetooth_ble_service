package com.alien.bluetooth_ble_service.ble_type.listener;

import android.bluetooth.le.AdvertiseSettings;
import android.util.Log;

public interface AdvertiseResultListener {

    String TAG = AdvertiseResultListener.class.getSimpleName();

    void onAdvertiseResult(AdvertiseSettings advertiseSettings);

    default void onAdvertiseFail(int errorCode) {
        Log.w(TAG, "onAdvertiseFail: " + errorCode);
    }

}
