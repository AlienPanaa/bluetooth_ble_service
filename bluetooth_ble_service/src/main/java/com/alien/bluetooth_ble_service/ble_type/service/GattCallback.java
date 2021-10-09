package com.alien.bluetooth_ble_service.ble_type.service;

import static com.alien.bluetooth_ble_service.ble_type.service.GattResultListener.ConnectionState.Connected;
import static com.alien.bluetooth_ble_service.ble_type.service.GattResultListener.ConnectionState.Connecting;
import static com.alien.bluetooth_ble_service.ble_type.service.GattResultListener.ConnectionState.Disconnected;
import static com.alien.bluetooth_ble_service.ble_type.service.GattResultListener.ConnectionState.Disconnecting;
import static com.alien.bluetooth_ble_service.ble_type.service.GattResultListener.ConnectionState.DiscoverServicesFail;
import static com.alien.bluetooth_ble_service.ble_type.service.GattResultListener.ConnectionState.DiscoveringServices;
import static com.alien.bluetooth_ble_service.ble_type.service.GattResultListener.GattAction.ConnectionState;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.ble_type.bean.ServicePackageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class GattCallback extends BluetoothGattCallback {

    private GattResultListener listener;

    GattCallback(GattResultListener listener) {
        setGattResultListener(listener);
    }

    public void setGattResultListener(@NonNull GattResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        switch (newState) {
            case BluetoothProfile.STATE_CONNECTING:
                listener.onConnectionStateChange(Connecting);
                break;

            case BluetoothProfile.STATE_CONNECTED:
                listener.onConnectionStateChange(Connected);

                if(!gatt.discoverServices()) {
                    listener.onConnectionStateChange(DiscoverServicesFail);
                } else {
                    listener.onConnectionStateChange(DiscoveringServices);
                }
                break;

            case BluetoothProfile.STATE_DISCONNECTED:
                listener.onConnectionStateChange(Disconnected);
                break;

            case BluetoothProfile.STATE_DISCONNECTING:
                listener.onConnectionStateChange(Disconnecting);
                break;
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        List<BluetoothGattService> services = gatt.getServices();
        List<ServicePackageInfo> info = new ArrayList<>(services.size());

        for(BluetoothGattService service : services) {
            if(service == null) continue;
            info.add(new ServicePackageInfo(service));
        }

        listener.onServicesDiscovered(info);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onCharacteristicData(GattResultListener.DataAction.Read, characteristic.getValue());
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onCharacteristicData(GattResultListener.DataAction.Write, characteristic.getValue());
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        listener.onCharacteristicData(GattResultListener.DataAction.Changed, characteristic.getValue());
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onDescriptorData(GattResultListener.DataAction.Read, descriptor.getValue());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onDescriptorData(GattResultListener.DataAction.Write, descriptor.getValue());
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onReliableWriteCompleted();
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onReadRemoteRssi(rssi);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onMtuChanged(mtu);
    }
    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onPhyData(GattResultListener.DataAction.Update, txPhy, rxPhy);
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            listener.onGattFail(ConnectionState);
            return;
        }

        listener.onPhyData(GattResultListener.DataAction.Read, txPhy, rxPhy);
    }

}
