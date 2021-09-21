package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothConnectSetting;

public class ServerConnection extends Connection {
    private static final String TAG = ServerConnection.class.getSimpleName();

    @Override
    public void connect(BluetoothConnectSetting setting, SocketListener socketListener) {
        String name = setting.getName() == null ? bluetoothAdapter.getName() : setting.getName();
        int timeout = setting.getTimeout();


        connectionAction(
                () -> {
                    BluetoothServerSocket mServerSocket =
                            bluetoothAdapter.listenUsingRfcommWithServiceRecord(name, setting.getUuid());

                    try (BluetoothSocket socket = mServerSocket.accept(timeout)) {
                        socketListener.onSocket(socket);
                    } catch (Exception e) {
                        e.printStackTrace();

                        setting.getTimeoutListener().onTimeout();   // TODO: 不一定是 Timeout
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
