package com.alien.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alien.bluetooth.databinding.ActivityMainBinding;
import com.alien.bluetooth_ble_service.base_view.BluetoothHandleRequest;
import com.alien.bluetooth_ble_service.basic_type.contoller.bluetooth_info.LocalBluetoothInfo;
import com.alien.bluetooth_ble_service.basic_type.setting.ScanSetting;
import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothController;
import com.alien.bluetooth_ble_service.bluetooth_type.service.BluetoothServiceBinder;
import com.alien.bluetooth_ble_service.bluetooth_type.setting.BluetoothSetting;

import java.util.Arrays;

public class MainActivity extends BluetoothHandleRequest {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private BluetoothListAdapter bluetoothListAdapter;
    private BluetoothServiceBinder bluetoothServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        boolean hasBluetooth = BluetoothController.getInstance().checkHardware(this);

        if(!hasBluetooth) {
            return;
        }

        if(BluetoothController.getInstance().requestPermission(this).length != 0) {
            BluetoothController.getInstance().requestBluetoothEnable(this);
        }

        initView();

        initBluetoothSetting();

        LocalBluetoothInfo localBluetoothInfo = BluetoothController.getInstance().getLocalBluetoothInfo(this);
        Log.i(TAG, "pairedDevice: " + localBluetoothInfo.getBondedDevices().toString());

    }

    private void initBluetoothSetting() {

        BluetoothSetting.Builder builder = new BluetoothSetting.Builder()
                .setErrorListener((errorCode, e) -> Log.w(TAG, "errorCode: " + errorCode, e));

        BluetoothController.getInstance().setBluetoothSetting(builder.build());


        BluetoothController.getInstance().startService(this, (isStart) -> {
            Log.i(TAG, "Start Service success");

            try {
                bluetoothServiceBinder = BluetoothController.getInstance().getServiceBinder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BluetoothController.getInstance().closeService(this);
    }

    @Override
    protected void discoverableBluetoothResult(boolean result) {
        super.discoverableBluetoothResult(result);

        boolean b = bluetoothServiceBinder.serverStartBroadcast();

    }

    private void initView() {
        bluetoothListAdapter = new BluetoothListAdapter();

        binding.bluetoothList.setLayoutManager(new LinearLayoutManager(this));
        binding.bluetoothList.setAdapter(bluetoothListAdapter);

        LocalBluetoothInfo localBluetoothInfo = BluetoothController.getInstance().getLocalBluetoothInfo(this);

        Log.i(TAG, "Local Bluetooth Info" + localBluetoothInfo.toString());


        binding.startService.setOnClickListener((v) -> BluetoothController.getInstance().ensureDiscoverable(this, 300));


        binding.readService.setOnClickListener(v -> {
            bluetoothServiceBinder.serverRead((len, cacheBuf) -> Log.i(TAG, "Len: " + len + ", buf: " + Arrays.toString(cacheBuf)));
        });

        binding.writeService.setOnClickListener(v -> bluetoothServiceBinder.serverWrite(new byte[]{0x11}));

        binding.readService.setOnClickListener(v -> {
            bluetoothServiceBinder.clientRead((len, cacheBuf) -> Log.i(TAG, "Len: " + len + ", buf: " + Arrays.toString(cacheBuf)));
        });

        binding.writeClient.setOnClickListener(v -> {
            bluetoothServiceBinder.clientWrite(new byte[]{0x22});
        });

        binding.clean.setOnClickListener(v -> bluetoothListAdapter.clean());

        binding.scan.setOnClickListener((v) -> {

            bluetoothServiceBinder.scanDevice(new ScanSetting()
                    .setIgnoreSame(true)
                    .setScanStateListener(isScanning -> Toast.makeText(this, "Is scanning? " + isScanning, Toast.LENGTH_SHORT).show())
                    .setBluetoothDeviceListener(bluetoothDevice -> {
                        String deviceName = bluetoothDevice.getName();
                        String deviceHardwareAddress = bluetoothDevice.getAddress(); // MAC address

                        Log.i(TAG, "deviceName: " + deviceName + ", address: " + deviceHardwareAddress);

                        bluetoothListAdapter.addDevice(bluetoothDevice, v1 -> bluetoothServiceBinder.clientConnectDevice(bluetoothDevice));
                    }));
        });

        binding.ble.setOnClickListener((v) -> startActivity(new Intent(this, MainActivity2.class)));
    }


}