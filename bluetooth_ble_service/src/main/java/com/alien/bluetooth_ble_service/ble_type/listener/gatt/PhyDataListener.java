package com.alien.bluetooth_ble_service.ble_type.listener.gatt;

@FunctionalInterface
public interface PhyDataListener {

    void onPhyData(DataAction action, int txPhy, int rxPhy);

}
