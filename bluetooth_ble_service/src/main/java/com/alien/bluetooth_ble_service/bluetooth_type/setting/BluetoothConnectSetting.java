package com.alien.bluetooth_ble_service.bluetooth_type.setting;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alien.bluetooth_ble_service.bluetooth_type.listener.ConnectErrorListener;

import java.util.UUID;

public class BluetoothConnectSetting {

    private static final String TAG = BluetoothConnectSetting.class.getSimpleName();

    public static final UUID defaultUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final String name;

    // min second
    private int timeout = 30_000;

    private ConnectErrorListener connectErrorListener = (e) -> Log.i(TAG, "Connect error.", e);

    private UUID uuid = UUID.randomUUID();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public BluetoothConnectSetting() {
        this(null);

        setUuid(defaultUuid);
    }

    public BluetoothConnectSetting(String name) {
        this.name = name;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public BluetoothConnectSetting setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    @NonNull
    public ConnectErrorListener getConnectErrorListener() {
        return connectErrorListener;
    }

    public void setConnectErrorListener(ConnectErrorListener connectErrorListener) {
        this.connectErrorListener = connectErrorListener;
    }
}
