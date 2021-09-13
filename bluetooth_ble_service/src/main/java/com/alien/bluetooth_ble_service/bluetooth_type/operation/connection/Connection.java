package com.alien.bluetooth_ble_service.bluetooth_type.operation.connection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothController;
import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothSetting;
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

    private final Map<UUID, ConnectionInfo> socketMap = new HashMap<>();

    protected final BluetoothAdapter bluetoothAdapter;
    protected final BluetoothSetting.Builder bluetoothSetting;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public Connection() {

        bluetoothSetting = BluetoothController.getInstance().getBluetoothSetting();

        this.bluetoothAdapter = bluetoothSetting.getBluetoothAdapter();
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
    public BluetoothReadWriteSocket getBluetoothReadWriteSocket() {
        UUID uuid = bluetoothSetting.getUuid();

        ConnectionInfo connectionInfo = socketMap.get(uuid);
        if(connectionInfo == null || connectionInfo.getBluetoothSocket() == null) {
            return null;
        }

        return connectionInfo.getReadWriteSocket();
    }

    private boolean cancelDiscovery() {
        return bluetoothAdapter.cancelDiscovery();
    }

    public final boolean connectDevice() {
        boolean result = true;

        UUID uuid = bluetoothSetting.getUuid();

        if(socketMap.containsKey(uuid) && socketMap.get(uuid) != null) {
            bluetoothSetting.getErrorListener()
                    .onError(BluetoothErrorListener.ALREADY_CONNECTED, new Exception("UUID already connect:" + uuid));

            return false;
        }

        if(!cancelDiscovery()) {
            result = false;

            bluetoothSetting.getErrorListener()
                    .onError(BluetoothErrorListener.CLIENT_CONNECT_FAIL, new Exception("Fail with uuid: " + uuid));
        }

        connect(uuid, (socket -> socketMap.put(uuid, new ConnectionInfo(socket))));

        return result;
    }


    public boolean closeDevice() {

        UUID uuid = bluetoothSetting.getUuid();

        ConnectionInfo connectionInfo = socketMap.get(uuid);

        if(!socketMap.containsKey(uuid) || connectionInfo == null || connectionInfo.getBluetoothSocket() == null) {
            bluetoothSetting.getErrorListener()
                    .onError(BluetoothErrorListener.HAVE_NOT_CONNECTED, new Exception("UUID haven't connect:" + uuid));

            return false;
        }

        boolean result = close(connectionInfo.getBluetoothSocket());

        socketMap.remove(uuid);

        return result;
    }

    protected abstract void connect(UUID uuid, SocketListener socketListener);

    protected abstract boolean close(@NonNull BluetoothSocket socket);

    protected void connectionAction(@NonNull RealAction realAction,
                                    @BluetoothErrorListener.ErrorType int errorCode) {


        executorService.submit(() -> {
            try {
                realAction.onAction();

            } catch (Exception e) {
                Log.e(TAG, "connectDevice method failed", e);

                bluetoothSetting.getErrorListener().onError(errorCode, e);
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

            bluetoothSetting.getErrorListener().onError(errorCode, e);

            return false;
        }

        return true;
    }

}
