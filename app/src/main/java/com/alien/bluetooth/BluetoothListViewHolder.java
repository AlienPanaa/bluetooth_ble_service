package com.alien.bluetooth;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alien.bluetooth.databinding.BluetoothItemBinding;

public final class BluetoothListViewHolder extends RecyclerView.ViewHolder {

    private final BluetoothItemBinding binding;

    public BluetoothListViewHolder(@NonNull BluetoothItemBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public void bind(String name) {
        binding.name.setText(name);
    }
}