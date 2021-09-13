package com.alien.bluetooth_ble_service.ble_type.service;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.listener.BluetoothDeviceListener;
import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;
import com.alien.bluetooth_ble_service.ble_type.controller.BLEController;
import com.alien.bluetooth_ble_service.ble_type.controller.BLESetting;
import com.alien.bluetooth_ble_service.ble_type.listener.BLEErrorListener;
import com.alien.bluetooth_ble_service.ble_type.listener.ScanResultListener;
import com.alien.bluetooth_ble_service.ble_type.scan.BLEScanAction;
import com.alien.bluetooth_ble_service.basic_type.listener.ScanStateListener;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothBinder;


public class BLEServiceBinder extends BluetoothBinder {

    private final BluetoothGattCallback callback;

    private BluetoothGatt bluetoothGatt;

    public BLEServiceBinder(Context context, BluetoothGattCallback callback) {
        super(context);

        this.callback = callback;
    }

    @NonNull
    @Override
    public BluetoothAdapter getBluetoothAdapter(@NonNull Context context) {
        BLESetting.Builder bluetoothSetting = BLEController.getInstance().getBluetoothSetting();

        return bluetoothSetting.getBluetoothAdapter() == null
                ? bluetoothSetting.getDefaultBluetoothAdapter(context) : bluetoothSetting.getBluetoothAdapter();

    }

    @Override
    public @NonNull
    BluetoothScan getBluetoothScanner(BluetoothAdapter bluetoothAdapter) {

        BluetoothDeviceListener bluetoothDeviceListener = BLEController.getInstance().getBluetoothSetting().getBluetoothDeviceListener();
        ScanStateListener scanStateListener = BLEController.getInstance().getBluetoothSetting().getScanStateListener();
        ScanResultListener scanResultListener = BLEController.getInstance().getBluetoothSetting().getScanResultListener();

        return new BLEScanAction(bluetoothAdapter, scanResultListener, bluetoothDeviceListener, scanStateListener);
    }

    @Override
    public synchronized boolean serverConnectDevice() {
        //TODO:
        return false;
    }

    @Override
    public synchronized boolean serverCloseDevice() {
        //TODO:
        return false;
    }

    @Override
    public synchronized boolean clientConnectDevice(BluetoothDevice device) {
        boolean autoConnect = BLEController.getInstance().getBluetoothSetting().isAutoConnect();

        bluetoothGatt = device.connectGatt(context, autoConnect, callback);

        return bluetoothGatt != null;
    }

    @Override
    public synchronized boolean clientCloseDevice() {
        if(bluetoothGatt == null) {
            BLEController.getInstance().getBluetoothSetting().getErrorListener().onError(BLEErrorListener.CLIENT_DISCONNECT_FAIL);
            return false;
        }
        bluetoothGatt.disconnect();
        return true;
    }

}
