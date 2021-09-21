package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothConnectSetting;

import java.util.UUID;

public class ServerConnection extends Connection {
    private static final String TAG = ServerConnection.class.getSimpleName();

    @Override
    public void connect(BluetoothConnectSetting setting, SocketListener socketListener) {
        String name = setting.getName() == null ? bluetoothAdapter.getName() : setting.getName();
        int timeout = setting.getTimeout();


        connectionAction(
                () -> {
                    UUID uuid = setting.getUuid();

                    BluetoothServerSocket mServerSocket =
                            bluetoothAdapter.listenUsingRfcommWithServiceRecord(name, uuid);

                    try {
                        BluetoothSocket socket = mServerSocket.accept(timeout);     // ms

                        socketListener.onSocket(socket);

                        Log.w(TAG, "Client " + uuid + " is connect success.");

                    } catch (Exception e) {
                        e.printStackTrace();

                        setting.getConnectErrorListener().onError(e);
                    }

                },
                BluetoothErrorListener.SERVER_CONNECT_FAIL
        );

    }

    @Override
    protected boolean close(@NonNull BluetoothSocket socket) {
        return closeAction(socket::close, BluetoothErrorListener.SERVER_DISCONNECT_FAIL);
    }

}
