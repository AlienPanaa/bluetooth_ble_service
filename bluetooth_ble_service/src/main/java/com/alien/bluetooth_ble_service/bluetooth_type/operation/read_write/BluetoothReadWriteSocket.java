package com.alien.bluetooth_ble_service.bluetooth_type.operation.read_write;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BluetoothReadWriteSocket {

    private static final String TAG = BluetoothReadWriteSocket.class.getSimpleName();
    private static final int CORE_THREAD_SIZE = 2;

    private BluetoothSocket socket;
    private final ExecutorService executorService = Executors.newFixedThreadPool(CORE_THREAD_SIZE);

    public interface MessageConstants {
        int MESSAGE_READ = 0;
    }

    public BluetoothReadWriteSocket(@NonNull BluetoothSocket socket) {
        this.socket = socket;
    }

    public void read(@NonNull Handler handler) {
        if(socket == null) {
            return;
        }

        executorService.submit(() -> {

            try(InputStream inputStream = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream)) {

                byte[] cacheBuf = new byte[1024];
                int len;

                while (true) {
                    try {
                        len = bis.read(cacheBuf);

                        if(len == -1) {
                            continue;
                        }

                        handler.obtainMessage(
                                MessageConstants.MESSAGE_READ,
                                len,
                                -1,
                                cacheBuf).sendToTarget();

                    } catch (IOException e) {
                        Log.d(TAG, "Input stream was disconnected", e);
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();

                executorService.shutdown();
            }
        });
    }

    public void write(byte[] bytes) {
        if(socket == null) {
            return;
        }

        executorService.submit(() -> {

            try(OutputStream outputStream = socket.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {

                bos.write(bytes);

            } catch (IOException e) {
                e.printStackTrace();

                executorService.shutdown();
            }
        });

    }

    public void close() {
        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }


}
