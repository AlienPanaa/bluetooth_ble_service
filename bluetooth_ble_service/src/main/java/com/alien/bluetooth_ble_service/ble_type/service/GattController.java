package com.alien.bluetooth_ble_service.ble_type.service;

import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.BEGIN_RELIABLE_WRITE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CHARACTERISTIC_READ;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CHARACTERISTIC_WRITE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.DESCRIPTOR_READ;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.EXE_RELIABLE_WRITE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.MTU_CHANGED;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.READ_REMOTE_RSSI;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.SDK_UNSUPPORTED;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.SERVICES_DISCOVERED;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Build;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.ble_type.listener.gatt.CharacteristicDataListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.DescriptorDataListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.MtuChangedListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.PhyDataListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.ReadRemoteRssiListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.ReliableWriteCompletedListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.ServicesDiscoveredListener;

public class GattController {

    private final GattCallback gattCallback;
    private final BluetoothGatt bluetoothGatt;

    public GattController(@NonNull GattCallback gattCallback, BluetoothGatt bluetoothGatt) {
        this.gattCallback = gattCallback;
        this.bluetoothGatt = bluetoothGatt;
    }

    private void fail(GattExceptionListener.GattAction action) {
        gattCallback.fail(false, action);
    }

    public GattController setGattExceptionListener(GattExceptionListener gattExceptionListener) {
        gattCallback.setGattExceptionListener(gattExceptionListener);
        return this;
    }

    public GattController beginReliableWrite() {
        if(bluetoothGatt.beginReliableWrite()) {
            fail(BEGIN_RELIABLE_WRITE);
        }
        bluetoothGatt.abortReliableWrite();
        return this;
    }

    public GattController executeReliableWrite(ReliableWriteCompletedListener reliableWriteCompletedListener) {
        gattCallback.setReliableWriteCompletedListener(reliableWriteCompletedListener);

        if(bluetoothGatt.executeReliableWrite()) {
            fail(EXE_RELIABLE_WRITE);
        }
        return this;
    }

    public GattController abortReliableWrite() {
        bluetoothGatt.abortReliableWrite();
        return this;
    }

    public GattController discoverServices(ServicesDiscoveredListener listener) {
        gattCallback.setServicesDiscoveredListener(listener);

        if(bluetoothGatt.discoverServices()) {
            fail(SERVICES_DISCOVERED);
        }
        return this;
    }

    public GattController readRssi(ReadRemoteRssiListener listener) {
        gattCallback.setReadRemoteRssiListener(listener);

        if(!bluetoothGatt.readRemoteRssi()) {
            fail(READ_REMOTE_RSSI);
        }
        return this;
    }

    public GattController readPhy(PhyDataListener listener) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            gattCallback.setPhyDataListener(listener);

            bluetoothGatt.readPhy();
        } else {
            fail(SDK_UNSUPPORTED);
        }
        return this;
    }

    public GattController requestMtu(int mtu, MtuChangedListener listener) {
        gattCallback.setMtuChangedListener(listener);

        if (!bluetoothGatt.requestMtu(mtu)) {
            fail(MTU_CHANGED);
        }
        return this;
    }

    public GattController readCharacteristic(BluetoothGattCharacteristic characteristic, CharacteristicDataListener listener) {
        gattCallback.setCharacteristicDataListener(listener);

        if(characteristic == null || !bluetoothGatt.readCharacteristic(characteristic)) {
            fail(CHARACTERISTIC_READ);
        }
        return this;
    }

    public GattController writeCharacteristic(BluetoothGattCharacteristic characteristic, CharacteristicDataListener listener) {
        gattCallback.setCharacteristicDataListener(listener);

        if(characteristic == null || !bluetoothGatt.writeCharacteristic(characteristic)) {
            fail(CHARACTERISTIC_WRITE);
        }
        return this;
    }

    public GattController readDescriptor(BluetoothGattDescriptor descriptor, DescriptorDataListener listener) {
        gattCallback.setDescriptorDataListener(listener);

        if(descriptor == null || bluetoothGatt.readDescriptor(descriptor)) {
            fail(DESCRIPTOR_READ);
        }
        return this;
    }

    public GattController writeDescriptor(BluetoothGattDescriptor descriptor, DescriptorDataListener listener) {
        gattCallback.setDescriptorDataListener(listener);

        if(descriptor == null || bluetoothGatt.writeDescriptor(descriptor)) {
            fail(DESCRIPTOR_READ);
        }
        return this;
    }
}
