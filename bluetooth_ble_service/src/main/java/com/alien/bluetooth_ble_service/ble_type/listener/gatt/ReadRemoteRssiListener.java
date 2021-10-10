package com.alien.bluetooth_ble_service.ble_type.listener.gatt;

@FunctionalInterface
public interface ReadRemoteRssiListener {

    void onReadRemoteRssi(int rssi);

}
