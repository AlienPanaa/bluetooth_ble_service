package com.alien.bluetooth_ble_service.basic_type.contoller;

import static android.content.Context.BIND_AUTO_CREATE;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.alien.bluetooth_ble_service.basic_type.listener.StartListener;
import com.alien.bluetooth_ble_service.basic_type.service.BluetoothCommonService;

import java.util.HashMap;

public abstract class ServiceInstanceController {

    private static final String TAG = ServiceInstanceController.class.getSimpleName();
    protected final HashMap<Integer, Object> bluetoothInstanceMap = new HashMap<>();

    private StartListener<Boolean> bluetoothStartListener;

    protected boolean saveInstance(int key, @NonNull Object instance) {
        boolean result = false;

        if(!bluetoothInstanceMap.containsKey(key)) {

            if(bluetoothInstanceMap.get(key) == null) {
                bluetoothInstanceMap.put(key, instance);

                result = true;
            } else {
                Log.i(TAG, "Already safe instance int BLE instance map: " + key);
            }
        } else {
            Log.i(TAG, "saveInstance, BLE instance map don't have: " + key);
        }

        return result;
    }

    protected void removeInstance(int key) {

        if(bluetoothInstanceMap.containsKey(key)) {
//            bluetoothInstanceMap.put(key, null);
            bluetoothInstanceMap.remove(key);
        } else {
            Log.i(TAG, "removeInstance, BLE instance map don't have: " + key);
        }
    }

    protected abstract Class<? extends BluetoothCommonService> getServiceClass();

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(saveInstance(getInstanceCode(), service) && bluetoothStartListener != null) {
                bluetoothStartListener.onStart(true);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {         // 主動斷線不會呼叫
            removeInstance(getInstanceCode());
        }
    };

    protected abstract int getInstanceCode();

    public void startService(@NonNull Context context) {
        startService(context, null);
    }

    public void startService(@NonNull Context context, @Nullable StartListener<Boolean> startListener) {
        this.bluetoothStartListener = startListener;

        Intent intent = new Intent(context, getServiceClass());
        context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    public void closeService(@NonNull Context context) {
        if(!bluetoothInstanceMap.containsKey(getInstanceCode())) {
            Log.e(TAG, "closeBLEService fail, because bluetooth haven't start");
            return;
        }
        removeInstance(getInstanceCode());
        context.unbindService(serviceConnection);
    }


    public Object getServiceBinder() throws Exception {
        if(!bluetoothInstanceMap.containsKey(getInstanceCode()) || bluetoothInstanceMap.get(getInstanceCode()) == null) {
            throw new Exception("Haven't start bluetooth service, please call 'startService' first.");
        }

        return bluetoothInstanceMap.get(getInstanceCode());
    }

}
