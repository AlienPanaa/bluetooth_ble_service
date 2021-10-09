package com.alien.bluetooth_ble_service.ble_type.service;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;
import com.alien.bluetooth_ble_service.ble_type.bean.AdvertiseInfo;
import com.alien.bluetooth_ble_service.ble_type.controller.BleController;
import com.alien.bluetooth_ble_service.ble_type.operation.advertise.BleAdvertise;
import com.alien.bluetooth_ble_service.ble_type.setting.BleScanSetting;
import com.alien.bluetooth_ble_service.ble_type.setting.BleSetting;
import com.alien.bluetooth_ble_service.ble_type.listener.BleErrorListener;
import com.alien.bluetooth_ble_service.ble_type.operation.scan.BleScanner;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothBinder;


public class BleServiceBinder extends BluetoothBinder {

    private final BluetoothGattCallback callback;

    private BluetoothGatt bluetoothGatt;

    private final BleAdvertise bleAdvertise;
    private final BleScanner bleScanner;

    public BleServiceBinder(Context context, BluetoothGattCallback callback) {
        super(context);

        this.callback = callback;
        this.bleAdvertise = new BleAdvertise();
        this.bleScanner = new BleScanner(bluetoothAdapter, this::clientConnect);
    }

    @NonNull
    @Override
    public BluetoothAdapter getBluetoothAdapter(@NonNull Context context) {
        BleSetting bluetoothSetting = BleController.getInstance().getBluetoothSetting();

        return bluetoothSetting.getBluetoothAdapter() == null
                ? bluetoothSetting.getDefaultBluetoothAdapter(context) : bluetoothSetting.getBluetoothAdapter();

    }

    public boolean searchDevice(ScanSetting scanSetting) {
        return bleScanner.startScan((BleScanSetting) scanSetting);    // TODO:
    }

    public synchronized boolean startBroadcast() {
        return startBroadcast(AdvertiseInfo.getDefault());
    }

    public synchronized boolean startBroadcast(@NonNull AdvertiseInfo advertiseInfo) {
        BluetoothLeAdvertiser bluetoothLeAdvertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
        if(bluetoothLeAdvertiser == null) {
            return false;
        }

        bleAdvertise.startAdvertise(bluetoothLeAdvertiser, advertiseInfo);
        return true;
    }

    public synchronized boolean closeBroadcast() {
        return bleAdvertise.stopAdvertise();
    }

    public synchronized boolean clientConnect(BluetoothDevice device) {
        boolean autoConnect = BleController.getInstance().getBluetoothSetting().isAutoConnect();

        bluetoothGatt = device.connectGatt(context, autoConnect, callback);

        return bluetoothGatt != null;
    }

    public synchronized boolean clientClose() {
        if(bluetoothGatt == null) {
            BleController.getInstance().getBluetoothSetting().getBleErrorListener().onError(BleErrorListener.CLIENT_DISCONNECT_FAIL);
            return false;
        }
        bluetoothGatt.disconnect();
        return true;
    }

}
