package com.alien.bluetooth_ble_service.bluetooth_type.setting;

import java.util.UUID;

public class BluetoothConnectSetting {

    private final String name;

    private int timeout = 60;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    private UUID uuid = UUID.randomUUID();

    public BluetoothConnectSetting() {
        this("Non");
    }

    public BluetoothConnectSetting(String name) {
        this.name = name;
    }

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

}
