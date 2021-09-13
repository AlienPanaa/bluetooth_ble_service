package com.alien.bluetooth_ble_service.base_view;

import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alien.bluetooth_ble_service.bluetooth_type.controller.BluetoothController;
import com.alien.bluetooth_ble_service.basic_type.contoller.CommonController;


public class BluetoothHandleRequest extends AppCompatActivity {

    private static final String TAG = BluetoothHandleRequest.class.getSimpleName();

    @CallSuper
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        String msg = "";

        switch (requestCode) {

            case CommonController.REQUEST_BLUETOOTH_PERMISSION:
                msg = getBluetoothPermissionDeniedHint();

                checkPermission(
                        permissions,
                        grantResults,
                        BluetoothController.REQUEST_BLUETOOTH_PERMISSION
                );
                break;

            case CommonController.REQUEST_ENABLE_BT:
                msg = getBluetoothDisEnableHint();

                checkPermission(
                        permissions,
                        grantResults,
                        BluetoothController.REQUEST_ENABLE_BT
                );
                break;

        }

        if(!msg.isEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }

    }


    private void checkPermission(@NonNull String[] permissions, @NonNull int[] grantResults, int requestCode) {

        for(int i = 0; i < grantResults.length; i++) {

            int result = grantResults[i];

            if(result == PackageManager.PERMISSION_DENIED) {
                String permission = permissions[i];

                Log.e(TAG, "PERMISSION_DENIED: " + permission);

                if(shouldShowRequestPermissionRationale(permission)) {   // 第一次拒絕後就會觸發 true，若是以確認不再同意則為 false

                    showTeachDialog(permission, requestCode);
                }
                break;
            }
        }

    }

    protected void showTeachDialog(@NonNull String permission, int requestCode) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Why use it")
                .setMessage(permission)
                .setNegativeButton("Allow", (dialog, which) -> {
                    requestPermissions(new String[] {permission}, requestCode);
                    dialog.dismiss();
                })
                .setNeutralButton("Think", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Leave", (dialog, which) -> finish())
                .create();

        alertDialog.show();
    }

    protected String getBluetoothPermissionDeniedHint() {
        return "Please granted permission.";
    }

    protected String getBluetoothDisEnableHint() {
        return "Please open bluetooth.";
    }


}
