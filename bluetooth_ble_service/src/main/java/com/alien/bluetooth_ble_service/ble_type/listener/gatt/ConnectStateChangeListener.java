package com.alien.bluetooth_ble_service.ble_type.listener.gatt;


@FunctionalInterface
public interface ConnectStateChangeListener {

    enum ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,

        DISCOVERING_SERVICES,
        DISCOVER_SERVICES_FAIL
    }

    void onConnectionStateChange(ConnectionState state);
}
