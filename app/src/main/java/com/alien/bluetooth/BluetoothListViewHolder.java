package com.alien.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alien.bluetooth.databinding.BluetoothItemBinding;
import com.alien.bluetooth_ble_service.bluetooth_type.service.BluetoothServiceBinder;

public final class BluetoothListViewHolder extends RecyclerView.ViewHolder {

    private final BluetoothItemBinding binding;

    public BluetoothListViewHolder(@NonNull BluetoothItemBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bind(BluetoothDevice device, View.OnClickListener listener) {
        binding.name.setText(device.getName() == null ? device.getAddress() : device.getName());

        binding.name.setOnClickListener(listener);
    }
}