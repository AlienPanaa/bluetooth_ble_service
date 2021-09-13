package com.alien.bluetooth_ble_service.basic_type.scan;

import android.bluetooth.BluetoothAdapter;
import android.os.CountDownTimer;
import android.util.Log;


public abstract class BluetoothScan {
    private static final String TAG = BluetoothScan.class.getSimpleName();

    private final BluetoothAdapter bluetoothAdapter;

    private CountDownTimer countDownTimer;

    public BluetoothScan(BluetoothAdapter bluetoothAdapter) {
        this.bluetoothAdapter = bluetoothAdapter;
    }

    public final boolean startScan(long scanTime) {
        boolean result;

        if(bluetoothAdapter.isDiscovering() && !stopScanAction(bluetoothAdapter)) {
            return false;

        }

        countDownTimer = new CountDownTimer(scanTime, 1000) {

            @Override
            public void onTick(long l) {
                Log.i(TAG, "Scanning: " + l);
            }

            @Override
            public void onFinish() {
                stopScanAction(bluetoothAdapter);

                scanFinishAction();

                countDownTimer.cancel();
            }

        }.start();

        result = startScanAction(bluetoothAdapter);

        return result;
    }

    protected void scanFinishAction() { }

    protected abstract boolean startScanAction(BluetoothAdapter bluetoothAdapter);

    protected abstract boolean stopScanAction(BluetoothAdapter bluetoothAdapter);

}
