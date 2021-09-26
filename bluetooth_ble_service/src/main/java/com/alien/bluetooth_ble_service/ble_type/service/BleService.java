package com.alien.bluetooth_ble_service.ble_type.service;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.alien.bluetooth_ble_service.basic_type.service.BluetoothCommonService;
import com.alien.bluetooth_ble_service.ble_type.bean.ServicePackageInfo;
import com.alien.bluetooth_ble_service.tools.UuidAdapter;

import java.util.List;
import java.util.UUID;


public class BleService extends BluetoothCommonService {

    private static final String TAG = BleService.class.getSimpleName();

    private final BluetoothGattCallback callback = new BluetoothGattCallback() {

        private boolean baseGattCheck(int status) {
            boolean result = false;

            switch (status) {
                case BluetoothProfile.STATE_DISCONNECTED:
                    result = true;
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    break;
            }

            if(!result) {
                // TODO: 好像有嘗試重新連接
            }

            return result;
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(!baseGattCheck(status)) {
                return;
            }

            switch (newState) {
                case BluetoothProfile.STATE_DISCONNECTED:
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    break;
                case BluetoothProfile.STATE_CONNECTED:
                    boolean isSuccess = gatt.discoverServices();

                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if(!baseGattCheck(status)) {
                return;
            }

            List<BluetoothGattService> services = gatt.getServices();

            for(BluetoothGattService service : services) {
                byte[] bytesFromUUID = UuidAdapter.getBytesFromUUID(service.getUuid());

                new ServicePackageInfo(bytesFromUUID);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        }
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BleServiceBinder(this, callback);
    }

}
