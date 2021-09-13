package com.alien.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alien.bluetooth.databinding.ActivityMainBinding;
import com.alien.module_bluetooth_ble.base_view.BluetoothHandleRequest;
import com.alien.module_bluetooth_ble.bluetooth_type.controller.BluetoothController;
import com.alien.module_bluetooth_ble.bluetooth_type.controller.BluetoothSetting;
import com.alien.module_bluetooth_ble.bluetooth_type.service.BluetoothServiceBinder;

import java.util.Set;

public class MainActivity extends BluetoothHandleRequest {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private BluetoothListAdapter bluetoothListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        BluetoothSetting.Builder builder = new BluetoothSetting.Builder()
                .setErrorListener((errorCode, e) ->
                        Log.w(TAG, "errorCode: " + errorCode, e));

        BluetoothController.getInstance().setBluetoothSetting(builder.build());


        initView();

        initEvent();

    }

    private void initEvent() {
        BluetoothController.getInstance().getBluetoothSetting()
                .setIgnoreSame(true)
                .setScanStateListener((isScanning) ->
                        Toast.makeText(this, "Is scanning? " + isScanning, Toast.LENGTH_SHORT).show())
                .setBluetoothDeviceListener((bluetoothDevice) -> {

                    String deviceName = bluetoothDevice.getName();
                    String deviceHardwareAddress = bluetoothDevice.getAddress(); // MAC address

                    Log.i(TAG, "deviceName: " + deviceName + ", address: " + deviceHardwareAddress);

                    bluetoothListAdapter.addDevice(deviceHardwareAddress);

                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BluetoothController.getInstance().closeService(this);
    }

    private void initView() {
        bluetoothListAdapter = new BluetoothListAdapter();

        binding.bluetoothList.setLayoutManager(new LinearLayoutManager(this));
        binding.bluetoothList.setAdapter(bluetoothListAdapter);

        binding.startService.setOnClickListener((v) -> {
            BluetoothController.getInstance().startService(this, (isStart) -> {
                Log.i(TAG, "Start Service success");


                boolean hasBluetooth = BluetoothController.getInstance().checkHardware(this);

                if(hasBluetooth) {
                    String[] losePermission = BluetoothController.getInstance()
                            .checkPermission(this);

                    if(losePermission.length == 0) {
                        BluetoothController.getInstance().requestBluetoothEnable(this);
                    }
                }

            });
        });

        binding.closeService.setOnClickListener((v) -> BluetoothController.getInstance().closeService(this));

        binding.scan.setOnClickListener((v) -> {
            try {
                BluetoothServiceBinder bluetoothServiceBinder = BluetoothController.getInstance().getServiceBinder();

                Set<BluetoothDevice> pairedDevice = bluetoothServiceBinder.getPairedDevice();

                Log.i(TAG, "pairedDevice: " + pairedDevice.toString());

                bluetoothServiceBinder.searchDevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.ble.setOnClickListener((v) -> startActivity(new Intent(this, MainActivity2.class)));
    }


}