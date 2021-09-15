package com.alien.bluetooth_ble_service.bluetooth_type.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.alien.bluetooth_ble_service.basic_type.service.BluetoothCommonService;
import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothController;

import java.util.HashSet;
import java.util.Set;


public class BluetoothService extends BluetoothCommonService {

    private static final String TAG = BluetoothService.class.getSimpleName();

    private static final String[] BLUETOOTH_FILTER = {
            BluetoothDevice.ACTION_FOUND,
            BluetoothAdapter.ACTION_DISCOVERY_STARTED,
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED,
    };

    private final Set<String> deviceRecord = new HashSet<>();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG, "Broadcast receive action: " + action);

            if(action == null) {
                return;
            }

            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    notifyScanResult(device);
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    notifyScanState(true);
                    break;

                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    notifyScanState(false);
                    break;

                default:
                    Log.i(TAG, "Unset Broadcast receive action: " + action);
                    break;
            }

        }

    };

    protected void notifyScanState(boolean isScanning) {
        ScanSetting scanSetting = BluetoothServiceBinder.getScanSetting();

        if(!isScanning) {
            deviceRecord.clear();
        }

        if(scanSetting != null) {
            scanSetting.getScanStateListener().onScanState(isScanning);
        }
    }


    protected void notifyScanResult(BluetoothDevice device) {
        ScanSetting scanSetting = BluetoothServiceBinder.getScanSetting();

        if(scanSetting != null) {
            boolean ignoreSame = scanSetting.isIgnoreSame();

            if(ignoreSame) {
                boolean add = deviceRecord.add(device.getAddress());

                if(!add) {
                    return;
                }
            }
            scanSetting.getBluetoothDeviceListener().onBluetoothDevice(device);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        registerIntentFilter();

        return new BluetoothServiceBinder(this);
    }

    @Override
    @CallSuper
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    private void registerIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();

        for(String item : BLUETOOTH_FILTER) {
            intentFilter.addAction(item);
            Log.i(TAG, "registerIntentFilter: " + item);
        }

        registerReceiver(receiver, intentFilter);
    }

}
