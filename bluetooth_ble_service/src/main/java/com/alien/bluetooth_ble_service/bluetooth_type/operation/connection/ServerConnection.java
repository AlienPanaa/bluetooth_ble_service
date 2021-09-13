package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;
import com.alien.bluetooth_ble_service.bluetooth_type.info_bean.BluetoothServerInfo;

import java.util.UUID;

public class ServerConnection extends Connection {
    private static final String TAG = ServerConnection.class.getSimpleName();

    @Override
    public void connect(UUID uuid, SocketListener socketListener) {
        BluetoothServerInfo serverInfo = bluetoothSetting.getServerInfo();

        String name = serverInfo.getName();
        int timeout = serverInfo.getTimeout();

        connectionAction(
                () -> {
                    BluetoothServerSocket mServerSocket =
                            bluetoothAdapter.listenUsingRfcommWithServiceRecord(name, uuid);

//                    while (true) {
//
//                        BluetoothSocket socket = mServerSocket.accept(timeout);   // block
//                        // TODO:
//
//                        close(uuid);
//                    }

                    try (BluetoothSocket socket = mServerSocket.accept(timeout)) {
                        socketListener.onSocket(socket);
                    } catch (Exception e) {
                        bluetoothSetting.getErrorListener().onError(BluetoothErrorListener.SERVER_CONNECT_FAIL, e);
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
