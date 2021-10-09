package com.alien.bluetooth_ble_service.ble_type.bean;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alien.bluetooth_ble_service.tools.UuidAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServicePackageInfo {
    private static final int AD_MAX_SIZE = 31;

    private final BluetoothGattService service;
    private final byte[] rawData;
    private final UUID rawUuid;
    private final List<CharacteristicPackageInfo> characteristicPackageInfoList;

    private ServiceInfo[] serviceInfo;

    public ServicePackageInfo(@NonNull BluetoothGattService service) {
        this.service = service;
        this.rawUuid = service.getUuid();
        this.rawData = UuidAdapter.getBytesFromUUID(service.getUuid());

        analysesService();

        characteristicPackageInfoList = new ArrayList<>(service.getCharacteristics().size());
        for(BluetoothGattCharacteristic chr : service.getCharacteristics()) {
            characteristicPackageInfoList.add(new CharacteristicPackageInfo(chr));
        }
    }

    private void analysesService() {
        ArrayList<ServiceInfo> info = new ArrayList<>();

        try {
            boolean isFinish = false;
            int index = 0;

            while (!isFinish) {
                ServiceInfo serviceInfo = new ServiceInfo();

                serviceInfo.length = rawData[index];
                serviceInfo.type = rawData[index + 1];

                int valueLen = serviceInfo.length - 1;
                serviceInfo.value = new byte[valueLen];
                System.arraycopy(
                        rawData,
                        index + 2,
                        serviceInfo.value,
                        0,
                        valueLen);

                info.add(serviceInfo);

                index = serviceInfo.length + 1;

                if(index >= AD_MAX_SIZE) {
                    isFinish = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        serviceInfo = info.toArray(new ServiceInfo[0]);
    }

    @Nullable
    public ServiceInfo[] getServiceInfo() {
        return serviceInfo;
    }

    @NonNull
    public byte[] getRawData() {
        return rawData;
    }

    @NonNull
    public UUID getRawUuid() {
        return rawUuid;
    }

    @NonNull
    public BluetoothGattService getService() {
        return service;
    }

    @NonNull
    public List<CharacteristicPackageInfo> getCharacteristicPackageInfoList() {
        return characteristicPackageInfoList;
    }

    public enum AdType {
        DEVICE_LE_TYPE(0x01),

        SUPPORT_INCOMPLETE_16BIT(0x02),
        SUPPORT_COMPLETE_16BIT(0x03),
        SUPPORT_INCOMPLETE_32BIT(0x04),
        SUPPORT_COMPLETE_32BIT(0x05),
        SUPPORT_INCOMPLETE_128BIT(0x06),
        SUPPORT_COMPLETE_128BIT(0x07),

        COMPLETE_DEVICE_NAME(0x08),
        ABBREVIATION_DEVICE_NAME(0x09),

        TX_POWER_LEVEL(0x0A),

        SERVICE_16BIT_UUID(0x16),
        SERVICE_32BIT_UUID(0x20),
        SERVICE_128BIT_UUID(0x21),

        OTHER(0xFF)
        ;
        int data;
        AdType(int data) {
            this.data = data;
        }

        static AdType pair(byte type) {
            AdType result = OTHER;

            for(AdType ad : values()) {
                if(ad.data == type) {
                    result = ad;
                    break;
                }
            }

            return result;

        }
    }

    public static class ServiceInfo {
        private int length = 0;
        private byte type = 0x00;
        private byte[] value = null;

        public int getLength() {
            return length;
        }

        public AdType getType() {
            return AdType.pair(type);
        }

        public byte getTypeData() {
            return type;
        }

        public byte[] getValue() {
            return value;
        }
    }
}
