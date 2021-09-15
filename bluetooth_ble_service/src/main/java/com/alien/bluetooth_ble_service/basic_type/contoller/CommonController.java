package com.alien.bluetooth_ble_service.basic_type.contoller;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;


import com.alien.bluetooth_ble_service.basic_type.contoller.bluetooth_info.LocalBluetoothInfo;
import com.alien.bluetooth_ble_service.basic_type.setting.CommonSetting;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CommonController<T extends CommonSetting>
        extends ServiceInstanceController {

    private static final String TAG = CommonController.class.getSimpleName();

    private static final String[] BLUETOOTH_PERMISSIONS = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final int REQUEST_ENABLE_BT = 0xA1;
    public static final int REQUEST_BLUETOOTH_PERMISSION = 0xA0;

    @NonNull
    public String[] checkPermission(@NonNull Activity activity) {
        ArrayList<String> arrayList = new ArrayList<>();

        for(String permission : BLUETOOTH_PERMISSIONS) {
            int result = activity.checkSelfPermission(permission);

            if(result != PackageManager.PERMISSION_GRANTED) {
                arrayList.add(permission);
            }
        }

        if(arrayList.size() == 0) {
            return new String[] {};
        }

        String[] requestItems = arrayList.toArray(new String[0]);
        Log.i(TAG, "Request: " + Arrays.toString(requestItems));

        activity.requestPermissions(requestItems, REQUEST_BLUETOOTH_PERMISSION);

        return requestItems;
    }

    public abstract T getBluetoothSetting();

    public abstract boolean checkHardware(@NonNull Context context);

    public LocalBluetoothInfo getLocalBluetoothInfo(@NonNull Context context) {
        CommonSetting bluetoothSetting = getBluetoothSetting();

        BluetoothAdapter adapter = bluetoothSetting.getBluetoothAdapter();
        if(adapter == null) {
            adapter = bluetoothSetting.getDefaultBluetoothAdapter(context);
        }

        return new LocalBluetoothInfo(adapter);
    }
    

    public CommonController<T> requestBluetoothEnable(@NonNull Activity activity) {
        CommonSetting bluetoothSetting = getBluetoothSetting();

        if(bluetoothSetting.getBluetoothAdapter() == null || !bluetoothSetting.getBluetoothAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        return this;
    }


}
