package com.alien.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alien.bluetooth.databinding.ActivityMain2Binding;
import com.alien.module_bluetooth_ble.ble_type.controller.BLEController;
import com.alien.module_bluetooth_ble.ble_type.controller.BLESetting;
import com.alien.module_bluetooth_ble.ble_type.service.BLEServiceBinder;

import java.util.Set;

public class MainActivity2 extends AppCompatActivity {
    private static final String TAG = MainActivity2.class.getSimpleName();

    private ActivityMain2Binding binding;
    private BluetoothListAdapter bluetoothListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2);

        BLESetting.Builder builder = new BLESetting.Builder()
                .setErrorListener((errorCode, e) ->
                        Log.w(TAG, "errorCode: " + errorCode, e));

        BLEController.getInstance().setBluetoothSetting(builder.build());


        initView();

        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        BLEController.getInstance().closeService(this);
    }

    private void initEvent() {
        BLEController.getInstance().getBluetoothSetting()
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

    private void initView() {
        bluetoothListAdapter = new BluetoothListAdapter();

        binding.bluetoothList.setLayoutManager(new LinearLayoutManager(this));
        binding.bluetoothList.setAdapter(bluetoothListAdapter);

        binding.startService.setOnClickListener((v) -> {
            BLEController.getInstance().startService(this, (isStart) -> {
                Log.i(TAG, "Start Service success");


                boolean hasBluetooth = BLEController.getInstance().checkHardware(this);

                if(hasBluetooth) {
                    String[] losePermission = BLEController.getInstance()
                            .checkPermission(this);

                    if(losePermission.length == 0) {
                        BLEController.getInstance().requestBluetoothEnable(this);
                    }
                }

            });
        });

        binding.closeService.setOnClickListener((v) -> BLEController.getInstance().closeService(this));

        binding.scan.setOnClickListener((v) -> {
            try {
                BLEServiceBinder bluetoothServiceBinder = BLEController.getInstance().getServiceBinder();

                Set<BluetoothDevice> pairedDevice = bluetoothServiceBinder.getPairedDevice();

                Log.i(TAG, "pairedDevice: " + pairedDevice.toString());

                bluetoothServiceBinder.searchDevice(10_000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}