package com.alien.bluetooth_ble_service.bluetooth_type.info_bean;

public class BluetoothServerInfo {

    private final String name;

    private int timeout = 60;

    public BluetoothServerInfo() {
        this("Non");
    }

    public BluetoothServerInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public BluetoothServerInfo setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

}
