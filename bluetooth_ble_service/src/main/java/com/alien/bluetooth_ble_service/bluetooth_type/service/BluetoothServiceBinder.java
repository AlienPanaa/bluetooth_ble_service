package com.alien.bluetooth_ble_service.bluetooth_type.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothController;
import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothBinder;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothConnectSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.connection.ClientConnection;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.connection.ServerConnection;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.read_write.BluetoothReadWriteSocket;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.scan.BluetoothScanner;

import java.util.UUID;


public class BluetoothServiceBinder extends BluetoothBinder {
    private static final String TAG = BluetoothServiceBinder.class.getSimpleName();

    private final ServerConnection serverConnection;
    private final ClientConnection clientConnection;
    private final BluetoothScanner bluetoothScanner;

    private static ScanSetting scanSetting;

    public BluetoothServiceBinder(Context context) {
        super(context);

        this.serverConnection = new ServerConnection();
        this.clientConnection = new ClientConnection();
        this.bluetoothScanner = new BluetoothScanner(bluetoothAdapter);
    }

    @NonNull
    @Override
    public BluetoothAdapter getBluetoothAdapter(@NonNull Context context) {
        BluetoothSetting bluetoothSetting = BluetoothController.getInstance().getBluetoothSetting();

        return bluetoothSetting.getBluetoothAdapter() == null
                ? bluetoothSetting.getDefaultBluetoothAdapter(context) : bluetoothSetting.getBluetoothAdapter();
    }

    public boolean searchDevice(ScanSetting scanSetting) {
        BluetoothServiceBinder.scanSetting = scanSetting;
        return bluetoothScanner.startScan(scanSetting);
    }

    @Nullable
    public static ScanSetting getScanSetting() {
        return scanSetting;
    }

    public synchronized boolean serverConnectDevice(@NonNull BluetoothConnectSetting setting) {
        return serverConnection.connectDevice(setting);
    }

    public synchronized boolean serverCloseDevice(@NonNull UUID uuid) {
        return serverConnection.closeDevice(uuid);
    }

    public synchronized boolean clientConnectDevice(@NonNull BluetoothDevice device, @NonNull BluetoothConnectSetting setting) {
        return clientConnection.setDevice(device).connectDevice(setting);
    }

    public synchronized boolean clientCloseDevice(@NonNull UUID uuid) {
        return clientConnection.closeDevice(uuid);
    }

    public void serverRead(@NonNull UUID uuid, @NonNull Handler handler) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = serverConnection.getBluetoothReadWriteSocket(uuid);

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.read(handler);
        }
    }

    public void serverWrite(@NonNull UUID uuid, byte[] bytes) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = serverConnection.getBluetoothReadWriteSocket(uuid);

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.write(bytes);
        }
    }

    public void clientRead(@NonNull UUID uuid, @NonNull Handler handler) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = clientConnection.getBluetoothReadWriteSocket(uuid);

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.read(handler);
        }
    }

    public void clientWrite(@NonNull UUID uuid, byte[] bytes) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = serverConnection.getBluetoothReadWriteSocket(uuid);

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.write(bytes);
        }
    }


}
