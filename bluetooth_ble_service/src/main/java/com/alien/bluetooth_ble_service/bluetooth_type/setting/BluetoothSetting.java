package com.alien.bluetooth_ble_service.bluetooth_type.setting;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;


import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.listener.BluetoothErrorListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;


public class BluetoothSetting extends CommonSetting {
    private static final String TAG = BluetoothSetting.class.getSimpleName();

    private final BluetoothErrorListener bluetoothErrorListener;

    BluetoothSetting(@NonNull Builder builder) {
        super(builder);
        this.bluetoothErrorListener = builder.bluetoothErrorListener;
    }

    public BluetoothErrorListener getBluetoothErrorListener() {
        return bluetoothErrorListener;
    }

    @Override
    public BluetoothAdapter getDefaultBluetoothAdapter(@NonNull Context context) {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothSetting getDefaultSetting() {
        return new Builder().build();
    }

    public static final class Builder extends CommonSetting.Builder {
        private BluetoothConnectSetting serverInfo;
        private BluetoothClientSetting clientInfo;

        private BluetoothErrorListener bluetoothErrorListener;

        public Builder setErrorListener(BluetoothErrorListener bluetoothErrorListener) {
            this.bluetoothErrorListener = bluetoothErrorListener;
            return this;
        }

        public BluetoothErrorListener getErrorListener() {
            return bluetoothErrorListener;
        }

        public void setServerInfo(BluetoothConnectSetting serverInfo) {
            this.serverInfo = serverInfo;
        }

        public BluetoothClientSetting getClientInfo() {
            return clientInfo;
        }

        public void setClientInfo(BluetoothClientSetting clientInfo) {
            this.clientInfo = clientInfo;
        }

        public BluetoothSetting build() {
            if(bluetoothErrorListener == null) {
                bluetoothErrorListener = (errorCode, e) -> Log.e(TAG, "BluetoothController error: " + errorCode);
            }

            if(serverInfo == null) {
                serverInfo = new BluetoothConnectSetting();
            }

            if(getBluetoothAdapter() == null) {
                setBluetoothAdapter(BluetoothAdapter.getDefaultAdapter());
            }

            return new BluetoothSetting(this);
        }

    }

}
