package com.alien.bluetooth_ble_service.ble_type.listener.gatt;

public interface GattExceptionListener {

    enum GattAction {
        CONNECTION,
        SERVICES_DISCOVERED,
        CHARACTERISTIC_READ,
        CHARACTERISTIC_WRITE,
        //        CHARACTERISTIC_CHANGED,
        DESCRIPTOR_READ,
        DESCRIPTOR_WRITE,
        BEGIN_RELIABLE_WRITE,
        EXE_RELIABLE_WRITE,
        READ_REMOTE_RSSI,
        MTU_CHANGED,
        PHY_UPDATE,
        PHY_READ,

        SDK_UNSUPPORTED,
    }

    void onGattException(boolean isResponse, GattAction action, Exception e);

    default void onGattException(boolean isResponse, GattAction action) {
        onGattException(isResponse, action, new Exception(action.toString()));
    }


}
