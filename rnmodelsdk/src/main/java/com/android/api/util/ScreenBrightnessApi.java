package com.android.api.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

public class ScreenBrightnessApi {
    private static ScreenBrightnessApi api = null;
    private static boolean ispermission = false;

    public static ScreenBrightnessApi getApi() {
        if (api == null){
            synchronized (ScreenBrightnessApi.class){
                if (api == null){
                    api = new ScreenBrightnessApi();
                }
            }
        }
        return api;
    }

    /**
     * 1.获取系统默认屏幕亮度值 屏幕亮度值范围（0-255）
     **/
    private int getScreenBrightness(final Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            return Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getScreenBrightnessProgress(final Context context){
        int currdata = getScreenBrightness(context);
        int brightnessSettingMaximumId = context.getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android");
        final int brightnessSettingMaximum = context.getResources().getInteger(brightnessSettingMaximumId);
        int pos = (int) (currdata* 100.0f / brightnessSettingMaximum );
        return pos;
    }

    public void setScreenBrightness(final Context context, int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context))
                return;
        }
        int currdata = getScreenBrightness(context);
        int brightnessSettingMaximumId = context.getResources().getIdentifier("config_screenBrightnessSettingMaximum", "integer", "android");
        final int brightnessSettingMaximum = context.getResources().getInteger(brightnessSettingMaximumId);

        int brightnessSettingMinimumId = context.getResources().getIdentifier("config_screenBrightnessSettingMinimum", "integer", "android");
        int brightnessSettingMinimum = context.getResources().getInteger(brightnessSettingMinimumId);

        // 首先需要设置为手动调节屏幕亮度模式
        setScreenManualMode(context);

        int birghtessValue = brightnessSettingMaximum * progress / 100;
        ContentResolver contentResolver = context.getContentResolver();
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, birghtessValue);
    }


    /**
     * 3.关闭光感，设置手动调节背光模式
     * <p>
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC 自动调节屏幕亮度模式值为1
     * <p>
     * SCREEN_BRIGHTNESS_MODE_MANUAL 手动调节屏幕亮度模式值为0
     **/
    private void setScreenManualMode(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context))
                return;
        }
        ContentResolver contentResolver = context.getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 4.非系统签名应用，引导用户手动授权修改Settings 权限
     **/
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1000;

    @TargetApi(Build.VERSION_CODES.M)
    public void allowModifySettings(final Activity activity) {
        // Settings.System.canWrite(MainActivity.this)
        // 检测是否拥有写入系统 Settings 的权限
        if (!Settings.System.canWrite(activity)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity,
                    android.R.style.Theme_Material_Light_Dialog_Alert);
            builder.setTitle("请开启修改屏幕亮度权限");
            builder.setMessage("请点击允许开启");
            // 拒绝, 无法修改
            builder.setNegativeButton("拒绝",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity,
                                    "您已拒绝修系统Setting的屏幕亮度权限", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
            builder.setPositiveButton("去开启",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 打开允许修改Setting 权限的界面
                            Intent intent = new Intent(
                                    Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri
                                    .parse("package:"
                                            + activity.getPackageName()));
                            activity.startActivityForResult(intent,
                                    REQUEST_CODE_WRITE_SETTINGS);
                        }
                    });
            builder.setCancelable(false);
            builder.show();
        }
    }

    public void onActivityResult(final Activity activity,int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Settings.System.canWrite方法检测授权结果
                if (Settings.System.canWrite(activity.getApplicationContext())) {
                    // 5.调用修改Settings屏幕亮度的方法 屏幕亮度值 200
                    ModifySettingsScreenBrightness(activity,200);
                    Toast.makeText(activity, "系统屏幕亮度值" + getScreenBrightness(activity),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "您已拒绝修系统Setting的屏幕亮度权限",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 5.修改Setting 中屏幕亮度值
     * <p>
     * 修改Setting的值需要动态申请权限 <uses-permission
     * android:name="android.permission.WRITE_SETTINGS"/>
     **/
    private void ModifySettingsScreenBrightness(final Activity activity, int birghtessValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(activity))
                return;
        }
        // 首先需要设置为手动调节屏幕亮度模式
        setScreenManualMode(activity);

        ContentResolver contentResolver = activity.getContentResolver();
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, birghtessValue);
    }
}
