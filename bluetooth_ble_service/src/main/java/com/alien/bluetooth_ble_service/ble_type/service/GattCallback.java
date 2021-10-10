package com.alien.bluetooth_ble_service.ble_type.service;

import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.CONNECTED;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.CONNECTING;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.DISCONNECTED;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.DISCONNECTING;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.DISCOVERING_SERVICES;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.ConnectStateChangeListener.ConnectionState.DISCOVER_SERVICES_FAIL;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CHARACTERISTIC_READ;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CHARACTERISTIC_WRITE;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.CONNECTION;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.DESCRIPTOR_READ;
import static com.alien.bluetooth_ble_service.ble_type.listener.gatt.GattExceptionListener.GattAction.DESCRIPTOR_WRITE;
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
import java.util.List;
import java.util.Objects;

final class GattCallback extends BluetoothGattCallback {

    private ConnectStateChangeListener connectStateChangeListener;
    private ServicesDiscoveredListener servicesDiscoveredListener;
    private CharacteristicDataListener characteristicDataListener;
    private DescriptorDataListener descriptorDataListener;
    private ReliableWriteCompletedListener reliableWriteCompletedListener;
    private ReadRemoteRssiListener readRemoteRssiListener;
    private MtuChangedListener mtuChangedListener;
    private PhyDataListener phyDataListener;

    private GattExceptionListener gattExceptionListener;

    public void setCharacteristicDataListener(CharacteristicDataListener characteristicDataListener) {
        this.characteristicDataListener = characteristicDataListener;
    }

    public void setConnectStateChangeListener(ConnectStateChangeListener connectStateChangeListener) {
        this.connectStateChangeListener = connectStateChangeListener;
    }

    public void setDescriptorDataListener(DescriptorDataListener descriptorDataListener) {
        this.descriptorDataListener = descriptorDataListener;
    }

    public void setGattExceptionListener(GattExceptionListener gattExceptionListener) {
        this.gattExceptionListener = gattExceptionListener;
    }

    public void setMtuChangedListener(MtuChangedListener mtuChangedListener) {
        this.mtuChangedListener = mtuChangedListener;
    }

    public void setPhyDataListener(PhyDataListener phyDataListener) {
        this.phyDataListener = phyDataListener;
    }

    public void setReadRemoteRssiListener(ReadRemoteRssiListener readRemoteRssiListener) {
        this.readRemoteRssiListener = readRemoteRssiListener;
    }

    public void setReliableWriteCompletedListener(ReliableWriteCompletedListener reliableWriteCompletedListener) {
        this.reliableWriteCompletedListener = reliableWriteCompletedListener;
    }

    public void setServicesDiscoveredListener(ServicesDiscoveredListener servicesDiscoveredListener) {
        this.servicesDiscoveredListener = servicesDiscoveredListener;
    }

    public void fail(boolean isResponse, GattExceptionListener.GattAction action) {
        if(gattExceptionListener == null) {
            return;
        }
        gattExceptionListener.onGattException(isResponse, action);
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

        if(connectStateChangeListener == null) {
            return;
        }

        switch (newState) {
            case BluetoothProfile.STATE_CONNECTING:
                connectStateChangeListener.onConnectionStateChange(CONNECTING);
                break;

            case BluetoothProfile.STATE_CONNECTED:
                connectStateChangeListener.onConnectionStateChange(CONNECTED);

                if(!gatt.discoverServices()) {
                    connectStateChangeListener.onConnectionStateChange(DISCOVER_SERVICES_FAIL);
                } else {
                    connectStateChangeListener.onConnectionStateChange(DISCOVERING_SERVICES);
                }
                break;

            case BluetoothProfile.STATE_DISCONNECTED:
                connectStateChangeListener.onConnectionStateChange(DISCONNECTED);
                break;

            case BluetoothProfile.STATE_DISCONNECTING:
                connectStateChangeListener.onConnectionStateChange(DISCONNECTING);
                break;
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

        if(servicesDiscoveredListener == null) return;

        servicesDiscoveredListener.onServicesDiscovered(info);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(CHARACTERISTIC_READ);
            return;
        }

        if(characteristicDataListener == null) return;

        characteristicDataListener.onCharacteristicData(DataAction.READ, characteristic.getValue());
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(CHARACTERISTIC_WRITE);
            return;
        }

        if(characteristicDataListener == null) return;

        characteristicDataListener.onCharacteristicData(DataAction.WRITE, characteristic.getValue());
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        if(characteristicDataListener == null) return;

        characteristicDataListener.onCharacteristicData(DataAction.CHANGED, characteristic.getValue());
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(DESCRIPTOR_READ);
            return;
        }

        if(descriptorDataListener == null) return;

        descriptorDataListener.onDescriptorData(DataAction.READ, descriptor.getValue());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(DESCRIPTOR_WRITE);
            return;
        }

        if(descriptorDataListener == null) return;

        descriptorDataListener.onDescriptorData(DataAction.WRITE, descriptor.getValue());
    }

    @Override
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(EXE_RELIABLE_WRITE);
            return;
        }

        if(reliableWriteCompletedListener == null) return;

        reliableWriteCompletedListener.onReliableWriteCompleted();
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(READ_REMOTE_RSSI);
            return;
        }

        if(readRemoteRssiListener == null) return;

        readRemoteRssiListener.onReadRemoteRssi(rssi);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(MTU_CHANGED);
            return;
        }

        if(mtuChangedListener == null) return;

        mtuChangedListener.onMtuChanged(mtu);
    }
    @Override
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(PHY_UPDATE);
            return;
        }

        if(phyDataListener == null) return;

        phyDataListener.onPhyData(DataAction.UPDATE, txPhy, rxPhy);
    }

    @Override
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        if(!Objects.equals(status, BluetoothGatt.GATT_SUCCESS)){
            fail(PHY_READ);
            return;
        }

        if(characteristicDataListener == null) return;

        phyDataListener.onPhyData(DataAction.READ, txPhy, rxPhy);
    }

}
