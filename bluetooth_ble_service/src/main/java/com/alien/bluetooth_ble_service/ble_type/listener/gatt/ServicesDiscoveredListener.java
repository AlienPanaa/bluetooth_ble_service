package com.alien.bluetooth_ble_service.ble_type.listener.gatt;

import com.alien.bluetooth_ble_service.ble_type.bean.ServicePackageInfo;

import java.util.List;

@FunctionalInterface
public interface ServicesDiscoveredListener {

    void onServicesDiscovered(List<ServicePackageInfo> servicesPackages);

}
