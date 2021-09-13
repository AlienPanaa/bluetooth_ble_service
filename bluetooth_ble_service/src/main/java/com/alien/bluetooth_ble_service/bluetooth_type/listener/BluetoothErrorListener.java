package com.alien.bluetooth_ble_service.bluetooth_type.listener;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface BluetoothErrorListener {

    int DEVICE_LOSE_BLUETOOTH = 1;

    int SERVER_CONNECT_FAIL = 2;
    int SERVER_DISCONNECT_FAIL = 3;

    int CLIENT_CONNECT_FAIL = 4;
    int CLIENT_DISCONNECT_FAIL = 5;

    int ALREADY_CONNECTED = 6;
    int HAVE_NOT_CONNECTED = 7;

    int CANCEL_DISCOVERY_FAIL = 8;

    @IntDef({
            DEVICE_LOSE_BLUETOOTH,
            SERVER_CONNECT_FAIL,
            SERVER_DISCONNECT_FAIL,
            CLIENT_CONNECT_FAIL,
            CLIENT_DISCONNECT_FAIL,
            ALREADY_CONNECTED,
            HAVE_NOT_CONNECTED,
            CANCEL_DISCOVERY_FAIL
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ErrorType {}

    default void onError(@ErrorType int errorCode) {
        onError(errorCode, new Exception("Error code: " + errorCode));
    }

    void onError(@ErrorType int errorCode, Exception e);

}
