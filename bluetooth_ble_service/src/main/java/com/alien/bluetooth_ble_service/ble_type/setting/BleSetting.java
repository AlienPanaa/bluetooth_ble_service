package com.alien.bluetooth_ble_service.ble_type.setting;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;
import com.alien.bluetooth_ble_service.ble_type.listener.BleErrorListener;
import com.alien.bluetooth_ble_service.ble_type.listener.ScanResultListener;

public class BleSetting extends CommonSetting {
    private static final String TAG = BleSetting.class.getSimpleName();

    private final AdvertiseInfo advertiseInfo;
    private final ScanInfo scanInfo;

    private final BleErrorListener BLEErrorListener;
    private final ScanResultListener scanResultListener;

    private final boolean autoConnect;

    BleSetting(@NonNull Builder builder) {
        super(builder);
        this.advertiseInfo = builder.getAdvertiseInfo();
        this.scanInfo = builder.getScanInfo();
        this.BLEErrorListener = builder.getBleErrorListener();
        this.scanResultListener = builder.getScanResultListener();
        this.autoConnect = builder.isAutoConnect();
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public AdvertiseInfo getAdvertiseInfo() {
        return advertiseInfo;
    }

    public ScanInfo getScanInfo() {
        return scanInfo;
    }

    public BleErrorListener getBleErrorListener() {
        return BLEErrorListener;
    }

    public ScanResultListener getScanResultListener() {
        return scanResultListener;
    }

    public BluetoothAdapter getDefaultBluetoothAdapter(@NonNull Context context) {
        BluetoothManager bluetoothManager =
                (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);

        return bluetoothManager.getAdapter();
    }

    public static BleSetting getDefaultSetting() {
        return new Builder().build();
    }


    public static final class Builder extends CommonSetting.Builder {
        private AdvertiseInfo advertiseInfo;
        private ScanInfo scanInfo;

        private BleErrorListener BleErrorListener;
        private ScanResultListener scanResultListener;

        private boolean autoConnect;

        public Builder setErrorListener(BleErrorListener BLEErrorListener) {
            this.BleErrorListener = BLEErrorListener;
            return this;
        }

        public AdvertiseInfo getAdvertiseInfo() {
            return advertiseInfo;
        }

        public Builder setAdvertiseInfo(AdvertiseInfo advertiseInfo) {
            this.advertiseInfo = advertiseInfo;
            return this;
        }

        public ScanInfo getScanInfo() {
            return scanInfo;
        }

        public void setScanInfo(ScanInfo scanInfo) {
            this.scanInfo = scanInfo;
        }

        public BleErrorListener getBleErrorListener() {
            return BleErrorListener;
        }

        public Builder setBleErrorListener(BleErrorListener bleErrorListener) {
            this.BleErrorListener = bleErrorListener;
            return this;
        }

        public ScanResultListener getScanResultListener() {
            return scanResultListener;
        }

        public Builder setScanResultListener(ScanResultListener scanResultListener) {
            this.scanResultListener = scanResultListener;
            return this;
        }

        public BleErrorListener getErrorListener() {
            return BleErrorListener;
        }

        public boolean isAutoConnect() {
            return autoConnect;
        }

        public Builder setAutoConnect(boolean autoConnect) {
            this.autoConnect = autoConnect;
            return this;
        }

        public BleSetting build() {
            if(BleErrorListener == null) {
                BleErrorListener = (errorCode, e) -> Log.e(TAG, "BluetoothController error: " + errorCode);
            }

            if(scanResultListener == null) {
                scanResultListener = result -> Log.i(TAG, result.toString());
            }

            if(getBluetoothDeviceListener() == null) {
                setScanResultListener(bluetoothDevice -> Log.i(TAG, "bluetoothDevice info: " + bluetoothDevice));
            }

            if(getScanStateListener() == null) {
                setScanStateListener(isScanning -> Log.i(TAG, "Bluetooth is scanning: " + isScanning));
            }

            if(scanInfo == null) {
                scanInfo = ScanInfo.getDefault();
            }

            if(advertiseInfo == null) {
                advertiseInfo = AdvertiseInfo.getDefault();
            }

            return new BleSetting(this);
        }

    }

}
