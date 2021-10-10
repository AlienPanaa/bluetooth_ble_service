package com.alien.bluetooth_ble_service.ble_type.listener.gatt;

@FunctionalInterface
public interface CharacteristicDataListener {

    void onCharacteristicData(DataAction action, byte[] bytes);

}
