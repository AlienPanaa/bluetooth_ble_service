package com.alien.bluetooth_ble_service.basic_type.exception;

import android.os.Build;

public class SdkUnSupportException extends Exception {

    public SdkUnSupportException(int targetCode) {
        super("Api request " + targetCode + ", Your driver is " + Build.VERSION.SDK_INT);
    }

}
