package com.alien.bluetooth_ble_service.ble_type.listener.gatt;


@FunctionalInterface
public interface ConnectStateChangeListener {

    enum ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,
        DISCOVER_SERVICES
    }

    void onConnectionStateChange(ConnectionState state);
}
