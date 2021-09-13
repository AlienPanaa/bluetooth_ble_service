package com.alien.bluetooth;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alien.bluetooth.databinding.BluetoothItemBinding;

import java.util.ArrayList;
import java.util.List;

public class BluetoothListAdapter extends RecyclerView.Adapter<BluetoothListViewHolder> {

    public final List<String> names = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void addDevice(String name) {
        names.add(name);

        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clean() {
        names.clear();

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
        holder.bind(names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

}