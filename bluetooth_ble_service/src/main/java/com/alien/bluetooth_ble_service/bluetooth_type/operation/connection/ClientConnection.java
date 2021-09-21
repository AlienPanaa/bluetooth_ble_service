package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;


import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothConnectSetting;

import java.util.UUID;

public class ClientConnection extends Connection {
    private static final String TAG = ClientConnection.class.getSimpleName();

    private BluetoothDevice device;

    public ClientConnection setDevice(BluetoothDevice device) {
        this.device = device;
        return this;
    }

    @Override
    protected void connect(BluetoothConnectSetting setting, SocketListener socketListener) {

        connectionAction(
                () -> {
                    UUID uuid = setting.getUuid();

                    BluetoothSocket socket;
                    if(setting.isSecureRfcommSocket()) {
                        socket = device.createRfcommSocketToServiceRecord(uuid);
                    } else {
                        socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                    }

                    if(!socket.isConnected()) {
                        socket.connect();
                    } else {
                        Log.w(TAG, uuid + " is connected");
                    }

                    socketListener.onSocket(socket);

                    Log.w(TAG, "Client " + uuid + " is connect success.");

                },
                BluetoothErrorListener.CLIENT_CONNECT_FAIL
        );

    }

    @Override
    protected boolean close(@NonNull BluetoothSocket socket) {
        return closeAction(socket::close, BluetoothErrorListener.CLIENT_DISCONNECT_FAIL);
    }

}
