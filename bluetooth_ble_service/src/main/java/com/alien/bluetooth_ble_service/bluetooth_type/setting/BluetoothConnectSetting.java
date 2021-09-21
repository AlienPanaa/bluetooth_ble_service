package com.alien.bluetooth_ble_service.bluetooth_type.setting;

import android.util.Log;

import androidx.annotation.Nullable;

import com.alien.bluetooth_ble_service.bluetooth_type.listener.TimeoutListener;

import java.util.UUID;

public class BluetoothConnectSetting {

    private static final String TAG = BluetoothConnectSetting.class.getSimpleName();

    public static final UUID defaultUuid = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");

    private final String name;

    // min second
    private int timeout = 30_000;

    private TimeoutListener timeoutListener = () -> Log.i(TAG, "Time out");

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

    public TimeoutListener getTimeoutListener() {
        return timeoutListener;
    }

    public void setTimeoutListener(TimeoutListener timeoutListener) {
        this.timeoutListener = timeoutListener;
    }
}
