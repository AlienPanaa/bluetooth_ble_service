package com.alien.bluetooth_ble_service.ble_type.listener.gatt;

@FunctionalInterface
public interface DescriptorDataListener {

    void onDescriptorData(DataAction action, byte[] bytes);

}
