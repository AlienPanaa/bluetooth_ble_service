package com.alien.bluetooth_ble_service.basic_type.listener;

import androidx.annotation.Nullable;

@FunctionalInterface
public interface Result<T> {
    void onResult(@Nullable T t);
}
