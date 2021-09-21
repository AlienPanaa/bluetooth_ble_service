package com.alien.bluetooth_ble_service.ble_type.setting;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;
import com.alien.bluetooth_ble_service.ble_type.bean.AdvertiseInfo;
import com.alien.bluetooth_ble_service.ble_type.bean.ScanInfo;
import com.alien.bluetooth_ble_service.ble_type.listener.BleErrorListener;
import com.alien.bluetooth_ble_service.ble_type.listener.ScanResultListener;

public class BleSetting extends CommonSetting {
    private static final String TAG = BleSetting.class.getSimpleName();

    private final BleErrorListener BLEErrorListener;

    private final boolean autoConnect;

    BleSetting(@NonNull Builder builder) {
        super(builder);
        this.BLEErrorListener = builder.getBleErrorListener();
        this.autoConnect = builder.isAutoConnect();
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public BleErrorListener getBleErrorListener() {
        return BLEErrorListener;
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
        private BleErrorListener BleErrorListener;

        private boolean autoConnect;        // TODO: 移除... 不該放這裡

        public Builder setErrorListener(BleErrorListener BLEErrorListener) {
            this.BleErrorListener = BLEErrorListener;
            return this;
        }

        public BleErrorListener getBleErrorListener() {
            return BleErrorListener;
        }

        public Builder setBleErrorListener(BleErrorListener bleErrorListener) {
            this.BleErrorListener = bleErrorListener;
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

            return new BleSetting(this);
        }

    }

}
