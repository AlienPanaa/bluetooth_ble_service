package com.alien.bluetooth_ble_service.ble_type.bean;

import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.ble_type.listener.AdvertiseResultListener;

import java.util.UUID;

public class AdvertiseInfo {
    private static final String TAG = AdvertiseInfo.class.getSimpleName();

    public static final ParcelUuid DEFAULT_UUID = ParcelUuid.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private AdvertiseData advertiseData;
    private AdvertiseData scanResponse;
    private AdvertiseSettings advertiseSettings;

    private AdvertiseResultListener advertiseResultListener = advertiseSettings -> Log.w(TAG, advertiseSettings.toString());

    @NonNull
    public AdvertiseResultListener getAdvertiseResultListener() {
        return advertiseResultListener;
    }

    public void setAdvertiseResultListener(@NonNull AdvertiseResultListener advertiseResultListener) {
        this.advertiseResultListener = advertiseResultListener;
    }

    // Handler setting -----------------------------------------------------------------------------------
    public void setScanResponse(AdvertiseData scanResponse) {
        this.scanResponse = scanResponse;
    }

    public AdvertiseData getScanResponse() {
        return scanResponse;
    }

    public AdvertiseData getAdvertiseData() {
        return advertiseData;
    }

    public AdvertiseInfo setAdvertiseData(AdvertiseData advertiseData) {
        this.advertiseData = advertiseData;
        return this;
    }

    public AdvertiseSettings getAdvertiseSettings() {
        return advertiseSettings;
    }

    public AdvertiseInfo setAdvertiseSettings(AdvertiseSettings advertiseSettings) {
        this.advertiseSettings = advertiseSettings;
        return this;
    }

    public static AdvertiseInfo getDefault() {

        AdvertiseSettings mAdvSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .setConnectable(true)
                .setTimeout(60_000) // unit: ms, max is 180_000ms, 0 is unlimited time
                .build();

        AdvertiseData mAdvData = new AdvertiseData.Builder()
                .setIncludeTxPowerLevel(true)
//                .setIncludeDeviceName(true)
                .addServiceUuid(DEFAULT_UUID)
//                .addManufacturerData(65535, androidId)
                .build();


        return new AdvertiseInfo().setAdvertiseData(mAdvData).setAdvertiseSettings(mAdvSettings);
    }
}
