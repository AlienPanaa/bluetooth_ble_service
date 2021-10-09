package com.alien.bluetooth_ble_service.basic_type.listener;

@FunctionalInterface
public interface Result<T> {
    void onResult(T t);
}
