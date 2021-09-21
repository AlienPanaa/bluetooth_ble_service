package com.alien.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alien.bluetooth.databinding.BluetoothItemBinding;
import com.alien.bluetooth_ble_service.bluetooth_type.service.BluetoothServiceBinder;

import java.util.ArrayList;
import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListViewHolder> {

    public final List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    public final List<View.OnClickListener> clickListeners = new ArrayList<>();


    @SuppressLint("NotifyDataSetChanged")
    public void addDevice(BluetoothDevice device, View.OnClickListener clickListener) {
        bluetoothDevices.add(device);
        clickListeners.add(clickListener);

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clean() {
        bluetoothDevices.clear();

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BluetoothListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        BluetoothItemBinding binding = BluetoothItemBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new BluetoothListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothListViewHolder holder, int position) {
        holder.bind(bluetoothDevices.get(position), clickListeners.get(position));
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

}