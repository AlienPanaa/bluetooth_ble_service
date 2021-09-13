package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import androidx.annotation.NonNull;


import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;

import java.util.UUID;

public class ClientConnection extends Connection {

    private BluetoothDevice device;

    public ClientConnection setDevice(BluetoothDevice device) {
        this.device = device;
        return this;
    }

    @Override
    protected void connect(UUID uuid, SocketListener socketListener) {

        connectionAction(
                () -> {
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);

                    socket.connect();

                    socketListener.onSocket(socket);
                },
                BluetoothErrorListener.CLIENT_CONNECT_FAIL
        );

    }

    @Override
    protected boolean close(@NonNull BluetoothSocket socket) {
        return closeAction(socket::close, BluetoothErrorListener.CLIENT_DISCONNECT_FAIL);
    }

}
