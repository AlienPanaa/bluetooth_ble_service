package com.alien.bluetooth_ble_service.ble_type.listener;

import android.bluetooth.le.ScanResult;
import android.util.Log;

import androidx.annotation.NonNull;


import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;

import java.util.List;

public interface ScanResultListener {
    String TAG = ScanResultListener.class.getSimpleName();

    enum ScanFailedType {
        ALREADY_STARTED(1),
        APPLICATION_REGISTRATION_FAILED(2),
        INTERNAL_ERROR(3),
        FEATURE_UNSUPPORTED(4),
        OUT_OF_HARDWARE_RESOURCES(5),
        SCANNING_TOO_FREQUENTLY(6);

        int value;
        ScanFailedType(int value) {
            this.value = value;
        }
    }

    void OnScanResult(@NonNull ScanResult result);

    default void OnScanResult(@NonNull ScanResult result, BluetoothDeviceListener listener) {
        if(listener != null) {
            listener.onBluetoothDevice(result.getDevice());
        }
        OnScanResult(result);
    }

    default void onBatchScanResults(@NonNull List<ScanResult> results) {
        Log.i(TAG, results.toString());
    }

    default void onScanFailed(int errorCode) {
        try {
            ScanFailedType scanFailedType = ScanFailedType.values()[errorCode];
            Log.i(TAG, "ScanFailedType: " + scanFailedType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
