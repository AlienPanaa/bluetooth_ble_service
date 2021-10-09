package com.alien.bluetooth_ble_service.ble_type.bean;

import android.bluetooth.BluetoothGattCharacteristic;

public class CharacteristicPackageInfo {

    private final BluetoothGattCharacteristic characteristic;

    private boolean isReadable;
    private boolean isWriteable;
    private boolean isNotifiable;

    public CharacteristicPackageInfo(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;

        analyses();
    }

    private void analyses() {
        initPermission();

        initProperties();
    }

    private void initPermission() {
        int permissions = characteristic.getPermissions();

        // TODO: ... 代辦
    }

    private void initProperties() {
        int properties = characteristic.getProperties();
        isReadable = (properties & BluetoothGattCharacteristic.PROPERTY_READ) > 0;
        isWriteable = (properties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0
                || (properties & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0;
        isNotifiable = (properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0
                || (properties & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0;
    }

    public boolean isReadable() {
        return isReadable;
    }

    public boolean isWriteable() {
        return isWriteable;
    }

    public boolean isNotifiable() {
        return isNotifiable;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }

}
