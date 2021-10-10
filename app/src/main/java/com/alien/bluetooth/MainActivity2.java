package com.alien.bluetooth;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alien.bluetooth.databinding.ActivityMain2Binding;
import com.alien.bluetooth_ble_service.ble_type.controller.BleController;
import com.alien.bluetooth_ble_service.ble_type.service.BleServiceBinder;
import com.alien.bluetooth_ble_service.ble_type.setting.BleScanSetting;
import com.alien.bluetooth_ble_service.ble_type.setting.BleSetting;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = MainActivity2.class.getSimpleName();

    private ActivityMain2Binding binding;
    private BluetoothListAdapter bluetoothListAdapter;
    private BleServiceBinder bleServiceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);


        boolean hasBluetooth = BleController.getInstance().checkHardware(this);

        if(!hasBluetooth) {
            return;
        }

        if(BleController.getInstance().requestPermission(this).length != 0) {
            BleController.getInstance().requestBluetoothEnable(this);
        }

        initView();

        initBluetoothSetting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BleController.getInstance().closeService(this);
    }

    private void initBluetoothSetting() {
        BleSetting.Builder builder = new BleSetting.Builder()
                .setErrorListener((errorCode, e) -> Log.w(TAG, "errorCode: " + errorCode, e));

        BleController.getInstance().setBluetoothSetting(builder.build());

        BleController.getInstance().startService(this, (isStart) -> {
            Log.i(TAG, "Start Service success");

            try {
                bleServiceBinder = BleController.getInstance().getServiceBinder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void initView() {
        bluetoothListAdapter = new BluetoothListAdapter();

        binding.bluetoothList.setLayoutManager(new LinearLayoutManager(this));
        binding.bluetoothList.setAdapter(bluetoothListAdapter);

        binding.startService.setOnClickListener((v) -> bleServiceBinder.startBroadcast());

        binding.closeService.setOnClickListener((v) -> bleServiceBinder.closeBroadcast());

        binding.clean.setOnClickListener(v -> bluetoothListAdapter.clean());

        binding.scan.setOnClickListener((v) -> {
            bleServiceBinder.searchDevice(new BleScanSetting()
                    .setIgnoreSame(true)
                    .setScanStateListener((isScanning) -> Toast.makeText(this, "Is scanning? " + isScanning, Toast.LENGTH_SHORT).show())
                    .setBluetoothDeviceListener((bluetoothDevice) -> {

                        String deviceName = bluetoothDevice.getName();
                        String deviceHardwareAddress = bluetoothDevice.getAddress(); // MAC address

                        Log.i(TAG, "deviceName: " + deviceName + ", address: " + deviceHardwareAddress);

                        bluetoothListAdapter.addDevice(bluetoothDevice, v1 -> {
                            bleServiceBinder.clientConnect(bluetoothDevice);
                        });

                    })
//                    .setConnectAddress("", isConnected -> {
//
//                    })
            );

            bleServiceBinder.getGattController()
                    .readRssi(rssi -> {

                    })
                    .setGattExceptionListener((isResponse, action, e) -> {

                    })
            ;

        });

    }
}