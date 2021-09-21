package com.alien.bluetooth_ble_service.bluetooth_type.operation.read_write;

public interface ReadResponseListener {

    void onReadResponse(int len, byte[] cacheBuf);

}
