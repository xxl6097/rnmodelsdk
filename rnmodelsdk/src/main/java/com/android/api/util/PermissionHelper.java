package com.android.api.util;

import android.Manifest;
import android.app.Activity;

import com.android.api.util.DeviceHelper;
import com.android.api.util.Logc;

public class PermissionHelper {
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WRITE_SETTINGS};


    private static final int RC_LOCATION_CONTACTS_PERM = 124;

    public static void checkPermisson(Activity activity){
//        new RxPermissions(activity)
//                .request(REQUESTED_PERMISSIONS)
//                .subscribe(granted -> {
//                    if (granted) {
//                        // All requested permissions are granted
//                        String info = DeviceHelper.getPhoneAllInfo(activity);
//                        Logc.i("11device info========"+info);
//                    } else {
//                        // At least one permission is denied
//                        Logc.i("222device info========");
//                        String info = DeviceHelper.getPhoneAllInfo(activity);
//                        Logc.i("11device info========"+info);
//                    }
//                });

        String info = DeviceHelper.getPhoneAllInfo(activity);
        Logc.i("11device info========"+info);
    }
}
