package com.alien.bluetooth_ble_service.ble_type.service;

import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.CONNECTED;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.CONNECTING;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.DISCONNECTED;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.DISCONNECTING;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.DISCOVER_SERVICES;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CHARACTERISTIC_READ;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CHARACTERISTIC_WRITE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CONNECTION;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.DESCRIPTOR_READ;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.DESCRIPTOR_WRITE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.DISCOVER_SERVICES_FAIL;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.EXE_RELIABLE_WRITE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.MTU_CHANGED;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.PHY_READ;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.PHY_UPDATE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.READ_REMOTE_RSSI;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.SERVICES_DISCOVERED;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;

import com.alien.bluetooth_ble_service.ble_type.bean.ServicePackageInfo;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.CharacteristicDataListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.DataAction;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.DescriptorDataListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.MtuChangedListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.PhyDataListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.ReadRemoteRssiListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.ReliableWriteCompletedListener;
import com.alien.bluetooth_ble_service.ble_type.listener.gatt.ServicesDiscoveredListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class GattCallback extends BluetoothGattCallback {

    private final Map<Thread, ConnectStateChangeListener> connectStateChangeListenerMap = new HashMap<>();
    private final Map<Thread, ServicesDiscoveredListener> servicesDiscoveredListenerMap = new HashMap<>();
    private final Map<Thread, CharacteristicDataListener> characteristicDataListenerMap = new HashMap<>();
    private final Map<Thread, DescriptorDataListener> descriptorDataListenerMap = new HashMap<>();
    private final Map<Thread, ReliableWriteCompletedListener> reliableWriteCompletedListenerMap = new HashMap<>();
    private final Map<Thread, ReadRemoteRssiListener> readRemoteRssiListenerMap = new HashMap<>();
    private final Map<Thread, MtuChangedListener> mtuChangedListenerMap = new HashMap<>();
    private final Map<Thread, PhyDataListener> phyDataListenerMap = new HashMap<>();

    private final Map<Thread, GattExceptionListener> gattExceptionListenerMap = new HashMap<>();

    public synchronized void setCharacteristicDataListener(CharacteristicDataListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        characteristicDataListenerMap.put(thread, listener);
    }

    public synchronized void setConnectStateChangeListener(ConnectStateChangeListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        connectStateChangeListenerMap.put(thread, listener);
    }

    public synchronized void setDescriptorDataListener(DescriptorDataListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        descriptorDataListenerMap.put(thread, listener);
    }

    public synchronized void setGattExceptionListener(GattExceptionListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        gattExceptionListenerMap.put(thread, listener);
    }

    public synchronized void setMtuChangedListener(MtuChangedListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        mtuChangedListenerMap.put(thread, listener);
    }

    public synchronized void setPhyDataListener(PhyDataListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        phyDataListenerMap.put(thread, listener);
    }

    public synchronized void setReadRemoteRssiListener(ReadRemoteRssiListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        readRemoteRssiListenerMap.put(thread, listener);
    }

    public synchronized void setReliableWriteCompletedListener(ReliableWriteCompletedListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        reliableWriteCompletedListenerMap.put(thread, listener);
    }

    public synchronized void setServicesDiscoveredListener(ServicesDiscoveredListener listener) {
        Thread thread = Thread.currentThread();
        if(listener == null) {
            return;
        }
        servicesDiscoveredListenerMap.put(thread, listener);
    }

    public void fail(boolean isResponse, GattExceptionListener.GattAction action) {
        for(GattExceptionListener listener : gattExceptionListenerMap.values()) {
            listener.onGattException(isResponse, action);
        }
    }

    private void fail(GattExceptionListener.GattAction action) {
        fail(true, action);
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(CONNECTION);
            return;
        }

        ConnectStateChangeListener.ConnectionState state;

        switch (newState) {
            case BluetoothProfile.STATE_CONNECTING:
                state = CONNECTING;
                break;

            case BluetoothProfile.STATE_CONNECTED:
                state = CONNECTED;
                break;

            case BluetoothProfile.STATE_DISCONNECTED:
                state = DISCONNECTED;
                break;

            case BluetoothProfile.STATE_DISCONNECTING:
                state = DISCONNECTING;
                break;

            default:
                return;
        }

        notifyConnectStateChange(state);

        if(state == CONNECTED) {
            if(!gatt.discoverServices()) {
                fail(DISCOVER_SERVICES_FAIL);
            } else {
                state = DISCOVER_SERVICES;

                notifyConnectStateChange(state);
            }
        }
    }

    private void notifyConnectStateChange(ConnectStateChangeListener.ConnectionState state) {
        for(ConnectStateChangeListener listener : connectStateChangeListenerMap.values()) {
            listener.onConnectionStateChange(state);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(SERVICES_DISCOVERED);
            return;
        }

        List<BluetoothGattService> services = gatt.getServices();
        List<ServicePackageInfo> info = new ArrayList<>(services.size());

        for(BluetoothGattService service : services) {
            if(service == null) continue;
            info.add(new ServicePackageInfo(service));
        }

        for(ServicesDiscoveredListener listener : servicesDiscoveredListenerMap.values()) {
            listener.onServicesDiscovered(info);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(CHARACTERISTIC_READ);
            return;
        }

        for(CharacteristicDataListener listener : characteristicDataListenerMap.values()) {
            listener.onCharacteristicData(DataAction.READ, characteristic.getValue());
        }
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(CHARACTERISTIC_WRITE);
            return;
        }

        for(CharacteristicDataListener listener : characteristicDataListenerMap.values()) {
            listener.onCharacteristicData(DataAction.WRITE, characteristic.getValue());
        }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        for(CharacteristicDataListener listener : characteristicDataListenerMap.values()) {
            listener.onCharacteristicData(DataAction.CHANGED, characteristic.getValue());
        }
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(DESCRIPTOR_READ);
            return;
        }

        for(DescriptorDataListener listener : descriptorDataListenerMap.values()) {
            listener.onDescriptorData(DataAction.READ, descriptor.getValue());
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(DESCRIPTOR_WRITE);
            return;
        }

        for(DescriptorDataListener listener : descriptorDataListenerMap.values()) {
            listener.onDescriptorData(DataAction.WRITE, descriptor.getValue());
        }
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(EXE_RELIABLE_WRITE);
            return;
        }

        for(ReliableWriteCompletedListener listener : reliableWriteCompletedListenerMap.values()) {
            listener.onReliableWriteCompleted();
        }
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(READ_REMOTE_RSSI);
            return;
        }

        for(ReadRemoteRssiListener listener : readRemoteRssiListenerMap.values()) {
            listener.onReadRemoteRssi(rssi);
        }
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(MTU_CHANGED);
            return;
        }

        for(MtuChangedListener listener : mtuChangedListenerMap.values()) {
            listener.onMtuChanged(mtu);
        }
    }
    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(PHY_UPDATE);
            return;
        }

        for(PhyDataListener listener : phyDataListenerMap.values()) {
            listener.onPhyData(DataAction.UPDATE, txPhy, rxPhy);
        }
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(PHY_READ);
            return;
        }

        for(PhyDataListener listener : phyDataListenerMap.values()) {
            listener.onPhyData(DataAction.READ, txPhy, rxPhy);
        }
    }

}
