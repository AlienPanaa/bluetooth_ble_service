package com.alien.bluetooth_ble_service.ble_type.listener.gatt;

@FunctionalInterface
public interface MtuChangedListener {

    void onMtuChanged(int mtu);

}
