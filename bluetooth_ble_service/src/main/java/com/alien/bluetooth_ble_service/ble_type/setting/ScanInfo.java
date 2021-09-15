package com.alien.bluetooth_ble_service.ble_type.setting;

import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import java.util.List;

public class ScanInfo {

    private List<ScanFilter> scanFilters;
    private ScanSettings scanSettings;

    public List<ScanFilter> getScanFilters() {
        return scanFilters;
    }

    public ScanInfo setScanFilters(List<ScanFilter> scanFilters) {
        this.scanFilters = scanFilters;
        return this;
    }

    public ScanSettings getScanSettings() {
        return scanSettings;
    }

    public ScanInfo setScanSettings(ScanSettings scanSettings) {
        this.scanSettings = scanSettings;
        return this;
    }

    public static ScanInfo getDefault() {
        ScanSettings build = new ScanSettings.Builder().build();

        return new ScanInfo().setScanSettings(build);
    }

}
