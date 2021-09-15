package com.alien.bluetooth_ble_service.ble_type.bean;

import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.os.ParcelUuid;

import java.util.UUID;

public class AdvertiseInfo {

    public static final ParcelUuid DEFAULT_UUID = ParcelUuid.fromString(UUID.randomUUID().toString());

    private AdvertiseData advertiseData;
    private AdvertiseData scanResponse;
    private AdvertiseSettings advertiseSettings;

    public AdvertiseData getScanResponse() {
        return scanResponse;
    }

    public void setScanResponse(AdvertiseData scanResponse) {
        this.scanResponse = scanResponse;
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
                .setTimeout(20000)
                .build();

        AdvertiseData mAdvData = new AdvertiseData.Builder()
                .setIncludeTxPowerLevel(true)
                .addServiceUuid(DEFAULT_UUID)
//                .addManufacturerData(65535, androidId)
                .setIncludeDeviceName(true)
                .build();


        return new AdvertiseInfo().setAdvertiseData(mAdvData).setAdvertiseSettings(mAdvSettings);
    }
}
