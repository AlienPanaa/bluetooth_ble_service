package com.alien.bluetooth_ble_service.bluetooth_type.service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothController;
import com.alien.bluetooth_ble_service.basic_type.scan.BluetoothScan;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothBinder;
import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.connection.ClientConnection;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.connection.ServerConnection;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.read_write.BluetoothReadWriteSocket;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.scan.BluetoothScanAction;


public class BluetoothServiceBinder extends BluetoothBinder {
    private static final String TAG = BluetoothServiceBinder.class.getSimpleName();

    private final ServerConnection serverConnection;
    private final ClientConnection clientConnection;

    public BluetoothServiceBinder(Context context) {
        super(context);

        serverConnection = new ServerConnection();
        clientConnection = new ClientConnection();
    }

    @NonNull
    @Override
    public BluetoothAdapter getBluetoothAdapter(@NonNull Context context) {
        BluetoothSetting.Builder bluetoothSetting = BluetoothController.getInstance().getBluetoothSetting();

        return bluetoothSetting.getBluetoothAdapter() == null
                ? bluetoothSetting.getDefaultBluetoothAdapter(context) : bluetoothSetting.getBluetoothAdapter();
    }

    @NonNull
    @Override
    public BluetoothScan getBluetoothScanner(BluetoothAdapter bluetoothAdapter) {
        return new BluetoothScanAction(bluetoothAdapter);
    }

    public boolean searchDevice() {
        return super.searchDevice(12_000);
    }

    @Override
    public synchronized boolean serverConnectDevice() {
        return serverConnection.connectDevice();
    }

    @Override
    public synchronized boolean serverCloseDevice() {
        return serverConnection.closeDevice();
    }

    @Override
    public synchronized boolean clientConnectDevice(BluetoothDevice device) {
        return clientConnection.setDevice(device).connectDevice();
    }

    @Override
    public synchronized boolean clientCloseDevice() {
        return clientConnection.closeDevice();
    }

    public void serverRead(@NonNull Handler handler) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = serverConnection.getBluetoothReadWriteSocket();

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.read(handler);
        }
    }

    public void serverWrite(byte[] bytes) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = serverConnection.getBluetoothReadWriteSocket();

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.write(bytes);
        }
    }

    public void clientRead(@NonNull Handler handler) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = clientConnection.getBluetoothReadWriteSocket();

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.read(handler);
        }
    }

    public void clientWrite(byte[] bytes) {
        BluetoothReadWriteSocket bluetoothReadWriteSocket = serverConnection.getBluetoothReadWriteSocket();

        if(bluetoothReadWriteSocket != null) {
            bluetoothReadWriteSocket.write(bytes);
        }
    }


}
