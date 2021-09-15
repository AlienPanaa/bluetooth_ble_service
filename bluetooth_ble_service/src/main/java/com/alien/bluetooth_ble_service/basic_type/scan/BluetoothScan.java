package com.alien.bluetooth_ble_service.basic_type.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.CountDownTimer;
import android.util.Log;

import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;


public abstract class BluetoothScan<T extends ScanSetting> {
    private static final String TAG = BluetoothScan.class.getSimpleName();

    private final BluetoothAdapter bluetoothAdapter;

    private CountDownTimer countDownTimer;

    public BluetoothScan(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public final boolean startScan(T scanSetting) {
        boolean result;

        if(bluetoothAdapter.isDiscovering() && !stopScanAction(bluetoothAdapter, scanSetting)) {
            return false;

        }

        countDownTimer = new CountDownTimer(scanSetting.getScanTime(), 1000) {

            @Override
            public void onTick(long l) {
                Log.i(TAG, "Scanning: " + l);
            }

            @Override
            public void onFinish() {
                stopScanAction(bluetoothAdapter, scanSetting);

                scanFinishAction(scanSetting);

                countDownTimer.cancel();
            }

        }.start();

        result = startScanAction(bluetoothAdapter, scanSetting);

        return result;
    }

    protected void scanFinishAction(ScanSetting scanSetting) { }

    protected abstract boolean startScanAction(BluetoothAdapter bluetoothAdapter, T scanSetting);

    protected abstract boolean stopScanAction(BluetoothAdapter bluetoothAdapter, T scanSetting);

}
