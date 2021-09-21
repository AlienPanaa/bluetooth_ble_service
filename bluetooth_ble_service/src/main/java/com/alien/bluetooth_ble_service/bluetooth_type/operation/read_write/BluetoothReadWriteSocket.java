package com.alien.bluetooth_ble_service.bluetooth_type.operation.read_write;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BluetoothReadWriteSocket {

    private static final String TAG = BluetoothReadWriteSocket.class.getSimpleName();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private BluetoothSocket socket;

    private InputStream inputStream;
    private BufferedOutputStream bos;

    private OutputStream outputStream;
    private BufferedInputStream bis;

    private boolean isReadLoopStart = false;

    public BluetoothReadWriteSocket(@NonNull BluetoothSocket socket) {
        this.socket = socket;
    }

    public void read(@NonNull ReadResponseListener readResponseListener) {
        if(socket == null || isReadLoopStart) {
            return;
        }

        isReadLoopStart = true;

        executorService.submit(() -> {

            try {
                inputStream = socket.getInputStream();
//                bis = new BufferedInputStream(inputStream);

                byte[] cacheBuf = new byte[10];
                int len;

                while (isReadLoopStart) {
                    try {
//                        len = bis.read(cacheBuf);
                        len = inputStream.read(cacheBuf);

                        if(len == -1) {
                            continue;
                        }
                        Log.i(TAG, "write: " + Arrays.toString(cacheBuf));

                        readResponseListener.onReadResponse(len, cacheBuf);

                    } catch (IOException e) {
                        Log.d(TAG, "Input stream was disconnected", e);
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void write(byte[] bytes) {
        if(socket == null) {
            return;
        }

        executorService.submit(() -> {

            try {
                outputStream = socket.getOutputStream();
                bos = new BufferedOutputStream(outputStream);

                Log.i(TAG, "write: " + Arrays.toString(bytes));

                bos.write(bytes);

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }

    public synchronized void close() {
        try {
            if(bis != null) {
                bis.close();
            }

            if(inputStream != null) {
                inputStream.close();
            }

            if(bos != null) {
                bos.close();
            }

            if(outputStream != null) {
                outputStream.close();
            }

            if(socket != null) {
                socket.close();
                socket = null;
            }
            isReadLoopStart = false;

        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }


}
