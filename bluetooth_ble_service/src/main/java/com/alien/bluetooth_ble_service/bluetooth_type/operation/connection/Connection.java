package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothController;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothConnectSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;
import com.alien.bluetooth_ble_service.bluetooth_type.operation.read_write.BluetoothReadWriteSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Connection {

    private static final String TAG = Connection.class.getSimpleName();
    private static final int THREAD_POOL_SIZE = 5;

    private static final Map<UUID, ConnectionInfo> uuidSocketMap = new HashMap<>();

    protected final BluetoothAdapter bluetoothAdapter;
    private final BluetoothErrorListener bluetoothErrorListener;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public Connection() {

        bluetoothErrorListener = BluetoothController.getInstance().getBluetoothSetting().getBluetoothErrorListener();

        this.bluetoothAdapter = BluetoothController.getInstance().getBluetoothSetting().getBluetoothAdapter();
    }

    @FunctionalInterface
    protected interface RealAction {
        void onAction() throws Exception;
    }

    @FunctionalInterface
    public interface SocketListener {
        void onSocket(BluetoothSocket socket);
    }

    @Nullable
    public static BluetoothReadWriteSocket getBluetoothReadWriteSocket(UUID uuid) {
        ConnectionInfo connectionInfo = uuidSocketMap.get(uuid);
        if(connectionInfo == null || connectionInfo.getBluetoothSocket() == null) {
            return null;
        }

        return connectionInfo.getReadWriteSocket();
    }

    public final boolean connectDevice(BluetoothConnectSetting setting) {
        UUID uuid = setting.getUuid();

        if(uuidSocketMap.containsKey(uuid) && uuidSocketMap.get(uuid) != null) {
            bluetoothErrorListener.onError(BluetoothErrorListener.ALREADY_CONNECTED, new Exception("UUID already connect:" + uuid));

            return false;
        }

        bluetoothAdapter.cancelDiscovery();

        connect(setting, (socket -> uuidSocketMap.put(uuid, new ConnectionInfo(socket))));

        return true;
    }


    public boolean closeDevice(UUID uuid) {

        ConnectionInfo connectionInfo = uuidSocketMap.get(uuid);

        if(!uuidSocketMap.containsKey(uuid) || connectionInfo == null || connectionInfo.getBluetoothSocket() == null) {
            bluetoothErrorListener.onError(BluetoothErrorListener.HAVE_NOT_CONNECTED, new Exception("UUID haven't connect:" + uuid));

            return false;
        }

        BluetoothSocket bluetoothSocket = connectionInfo.getBluetoothSocket();

        connectionInfo.getReadWriteSocket().close();

        boolean result = close(bluetoothSocket);

        uuidSocketMap.remove(uuid);

        return result;
    }

    protected abstract void connect(BluetoothConnectSetting setting, SocketListener socketListener);

    protected abstract boolean close(@NonNull BluetoothSocket socket);

    protected void connectionAction(@NonNull RealAction realAction,
                                    @BluetoothErrorListener.ErrorType int errorCode) {


        executorService.submit(() -> {
            try {
                realAction.onAction();

            } catch (Exception e) {
                Log.e(TAG, "connectDevice method failed", e);

                bluetoothErrorListener.onError(errorCode, e);
            }
        });
    }

    protected boolean closeAction(@NonNull RealAction realAction, @BluetoothErrorListener.ErrorType int errorCode) {
        try {

            synchronized(this) {
                realAction.onAction();
            }

        } catch (Exception e) {
            Log.e(TAG, "Could not close the connect socket", e);

            bluetoothErrorListener.onError(errorCode, e);

            return false;
        }

        return true;
    }

}
