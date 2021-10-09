package com.alien.bluetooth_ble_service.ble_type.service;

import android.util.Log;

import com.alien.bluetooth_ble_service.ble_type.bean.ServicePackageInfo;

import java.util.Arrays;
import java.util.List;

@FunctionalInterface
public interface GattResultListener {
    String TAG = GattResultListener.class.getSimpleName();

    enum GattAction {
        ConnectionState,
    }

    enum ConnectionState {
        Connecting,
        Connected,
        Disconnecting,
        Disconnected,

        DiscoveringServices,
        DiscoverServicesFail
    }

    enum DataAction {
        Read,
        Write,
        Changed,
        Update,
    }

    void onGattFail(GattAction action);

    default void onConnectionStateChange(ConnectionState state) {
        Log.e(TAG, "onConnectionStateChange: " + state);
    }

    default void onServicesDiscovered(List<ServicePackageInfo> servicesPackages) {
        Log.e(TAG, "onServicesDiscovered: " + servicesPackages.size());
    }

    default void onCharacteristicData(DataAction action, byte[] bytes) {
        Log.e(TAG, "onCharacteristicData: " + action + ", bytes: " + Arrays.toString(bytes));
    }

    default void onPhyData(DataAction action, int txPhy, int rxPhy) {
        Log.e(TAG, "onPhyData: " + action + ", tx: " + txPhy + ", rx: " + rxPhy);
    }

    default void onDescriptorData(DataAction action, byte[] bytes) {
        Log.e(TAG, "onDescriptorData: " + action + ", bytes: " + Arrays.toString(bytes));
    }

    default void onReliableWriteCompleted() {
        Log.e(TAG, "onReliableWriteCompleted");
    }

    default void onReadRemoteRssi(int rssi) {
        Log.e(TAG, "onReadRemoteRssi: " + rssi);
    }

    default void onMtuChanged(int mtu) {
        Log.e(TAG, "onMtuChanged: " + mtu);
    }

}
