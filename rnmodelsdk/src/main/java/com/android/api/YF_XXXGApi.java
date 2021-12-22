package com.android.api;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.example.yf_a64_api.YF_A64_API_Manager;
import com.android.api.util.DeviceHelper;
import com.android.api.util.ScreenBrightnessApi;
import com.android.api.util.VolumeManager;
import com.android.api.util.Logc;

public class YF_XXXGApi {
    private Context context;
    private Activity activity;
    private YF_A64_API_Manager yfapi = null;

    public YF_XXXGApi(Activity act) {
        Logc.i("1YF_XXXGApi.构造============" + act);
        this.activity = act;
        if (activity != null) {
            VolumeManager.getInstance().init(activity, new Handler());
            ScreenBrightnessApi.getApi().allowModifySettings(activity);
            yfapi = new YF_A64_API_Manager(activity);
        }
    }

    public Bundle invoke(final Context c, final Bundle readableMap/*, final Callback callback*/) {
        this.context = c;
        Bundle writableMap = new Bundle();
        writableMap.putInt("code", 0);
        writableMap.putString("msg", "sucess");
        if (readableMap == null) {
            writableMap.putInt("code", -1);
            writableMap.putString("msg", "readableMap is null");
            Logc.i("==>readableMap is null");
            return writableMap;
        }
        String methodName = readableMap.getString("methodName");
        if (TextUtils.isEmpty(methodName)) {
            writableMap.putInt("code", -1);
            writableMap.putString("msg", "methodName value is null");
            return writableMap;
        }
        Logc.i(readableMap.toString());
        if (methodName.equalsIgnoreCase("allInfo")) {
            writableMap.putString("mac", getMac());
            writableMap.putString("ip", getIp());
            writableMap.putString("netType", getNetType());
            writableMap.putString("wifiName", getWiFiName());
            writableMap.putString("model", getModel());
            writableMap.putString("brand", getBrand());
            writableMap.putDouble("rawSize", getRAMSize());
            writableMap.putDouble("storageMemory", getStorageMemory());
            writableMap.putDouble("availableStorageMemorySize", getAvailablegetStorageMemorySize());
            writableMap.putInt("screenWidth", getScreenWidth());
            writableMap.putInt("screenHeight", getScreenHeight());
            writableMap.putInt("brightness", getBrightness());
            writableMap.putInt("volume", getVolume());
        } else if (methodName.equalsIgnoreCase("mac")) {
            writableMap.putString("mac", getMac());
        } else if (methodName.equalsIgnoreCase("ip")) {
            writableMap.putString("ip", getIp());
        } else if (methodName.equalsIgnoreCase("netType")) {
            writableMap.putString("netType", getNetType());
        } else if (methodName.equalsIgnoreCase("wifiName")) {
            writableMap.putString("wifiName", getWiFiName());
        } else if (methodName.equalsIgnoreCase("model")) {
            writableMap.putString("model", getModel());
        } else if (methodName.equalsIgnoreCase("brand")) {
            writableMap.putString("brand", getBrand());
        } else if (methodName.equalsIgnoreCase("rawSize")) {
            writableMap.putDouble("rawSize", getRAMSize());
        } else if (methodName.equalsIgnoreCase("storageMemory")) {
            writableMap.putDouble("storageMemory", getStorageMemory());
        } else if (methodName.equalsIgnoreCase("availableStorageMemorySize")) {
            writableMap.putDouble("availableStorageMemorySize", getAvailablegetStorageMemorySize());
        } else if (methodName.equalsIgnoreCase("screenWidth")) {
            writableMap.putInt("screenWidth", getScreenWidth());
        } else if (methodName.equalsIgnoreCase("screenHeight")) {
            writableMap.putInt("screenHeight", getScreenHeight());
        } else if (methodName.equalsIgnoreCase("brightness")) {
            writableMap.putInt("brightness", getBrightness());
        } else if (methodName.equalsIgnoreCase("volume")) {
            writableMap.putInt("volume", getVolume());
        } else if (methodName.equalsIgnoreCase("setVolume")) {
            int volume = readableMap.getInt("volume");
            if(volume == 0){
                Double dd = readableMap.getDouble("volume");
                volume = dd.intValue();

            }
            setVolume(volume);
        } else if (methodName.equalsIgnoreCase("setScreenBrightness")) {
            int brightness = readableMap.getInt("brightness");
            if(brightness == 0){
                Double dd = readableMap.getDouble("brightness");
                brightness = dd.intValue();

            }
            setScreenBrightness(brightness);
        } else if (methodName.equalsIgnoreCase("shutdown")) {
            shutdown();
        } else if (methodName.equalsIgnoreCase("reboot")) {
            reboot();
        } else if (methodName.equalsIgnoreCase("setOnOffTime")) {
            setOnOffTime(readableMap);
        } else if (methodName.equalsIgnoreCase("setLCDOn")) {
            setLCDOn();
        } else if (methodName.equalsIgnoreCase("setLCDOff")) {
            setLCDOff();
        } else if (methodName.equalsIgnoreCase("setRotation")) {
            String degree = readableMap.getString("degree");
            setRotation(degree);
        } else if (methodName.equalsIgnoreCase("setNavigationBarVisibility")) {
            Boolean status = readableMap.getBoolean("status");
            setNavigationBarVisibility(status);
        } else if (methodName.equalsIgnoreCase("setStatusBarDisplay")) {
            Boolean status = readableMap.getBoolean("status");
            setStatusBarDisplay(status);
        } else if (methodName.equalsIgnoreCase("setStatusBarVisibility")) {
            Boolean status = readableMap.getBoolean("status");
            setStatusBarVisibility(status);
        } else if (methodName.equalsIgnoreCase("setSysTime")) {
            Boolean status = readableMap.getBoolean("status");
            setSysTime(status);
        } else if (methodName.equalsIgnoreCase("gotoWiFi")) {
            DeviceHelper.gotoWiFi(activity);
        } else if (methodName.equalsIgnoreCase("gotoLanguage")) {
            DeviceHelper.gotoLanguage(activity);
        }
        Logc.i("发给RN的数据:" + writableMap.toString());
        return writableMap;
    }

    public String getMac() {
        return DeviceHelper.getDeviceMacAddress(context);
    }

    public String getIp() {
        return DeviceHelper.getLocalIP(context);
    }

    public String getWiFiName() {
        return DeviceHelper.getSSid(context);
    }

    public String getNetType() {
        return DeviceHelper.getNetType(context);
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getBrand() {
        return Build.BRAND;
    }

    public Integer getVolume() {
        return VolumeManager.getInstance().getMusicVolume();
    }

    public Integer getBrightness() {
        return ScreenBrightnessApi.getApi().getScreenBrightnessProgress(context);
    }

    public Double getRAMSize() {
        return DeviceHelper.getRAMSize();
    }

    public Double getStorageMemory() {
        return DeviceHelper.getStorageMemory();
    }

    public Double getAvailablegetStorageMemorySize() {
        return DeviceHelper.getAvailablegetStorageMemorySize();
    }

    public int getScreenWidth() {
        return DeviceHelper.getScreenWidth(context);
    }

    public int getScreenHeight() {
        return DeviceHelper.getScreenHeight(context);
    }

    public Integer setVolume(Integer volume) {
        Logc.i("setVolume " + volume);
        VolumeManager.getInstance().setVolumeMusic(volume);
        return volume;
    }

    public int setScreenBrightness(Integer value) {
        Logc.i("setScreenBrightness " + value);
        ScreenBrightnessApi.getApi().setScreenBrightness(context, value);
        int ness = ScreenBrightnessApi.getApi().getScreenBrightnessProgress(context);
        return ness;
    }

    public void shutdown() {
        if (yfapi != null) {
            Logc.i("shutdown ");
            yfapi.yfShutDown();
        }
    }

    public void reboot() {
        if (yfapi != null) {
            Logc.i("reboot ");
            yfapi.yfReboot();
        }
    }

    //设置系统开关机时间（设置的时间必须比当前时间大于3分钟，开机时间间隔大
    public void setOnOffTime(final Bundle readableMap) {
        if (yfapi != null) {
            Logc.i("setOnOffTime ");
            int[] timeonArray = {
                    readableMap.getInt("on_year"),
                    readableMap.getInt("on_month"),
                    readableMap.getInt("on_day"),
                    readableMap.getInt("on_hour"),
                    readableMap.getInt("on_minute")};
            int[] timeoffArray = {
                    readableMap.getInt("off_year"),
                    readableMap.getInt("off_month"),
                    readableMap.getInt("off_day"),
                    readableMap.getInt("off_hour"),
                    readableMap.getInt("off_minute")};
            yfapi.yfsetOnOffTime(timeonArray, timeoffArray, true);
        }
    }

    public void setLCDOn() {
        if (yfapi != null) {
            Logc.i("setLCDOff ");
            yfapi.yfSetLCDOn();
        }
    }

    //关闭屏背光函数，调用直接关闭屏背光
    public void setLCDOff() {
        if (yfapi != null) {
            Logc.i("setLCDOff ");
            yfapi.yfSetLCDOff();
        }
    }

    //设置系统旋转角度显示的接口，（调用以后自动重启生效）
    //Degree:可以有4种值（"0"，"90"，"180"，"270"），其他值默认为和"0"一样的处理方式
    public void setRotation(String degree) {
        if (yfapi != null) {
            Logc.i("setRotation " + degree);
            yfapi.yfsetRotation(degree);
        }
    }

    //设置按键导航栏的隐藏和显示
    //enable:true 表示设置导航栏显示出来，false表示设置导航栏隐藏掉z
    public void setNavigationBarVisibility(Boolean status) {
        if (yfapi != null) {
            Logc.i("setNavigationBarVisibility " + status);
            yfapi.yfsetNavigationBarVisibility(status);
        }
    }

    //设置系统是否有状态栏显示出来,设置以后自动重启生效。
    //enable:true 表示设置导航栏显示出来，false表示设置导航栏不显示，包括其占有的位置
    public void setStatusBarDisplay(Boolean status) {
        if (yfapi != null) {
            Logc.i("setStatusBarDisplay " + status);
            yfapi.yfsetStatusBarDisplay(status);
        }
    }

    //设置系统是否有状态栏显示还是隐藏，隐藏的时候还会占用位置，不用重启生效
    //enable:true 表示设置导航栏显示出来，false表示设置导航栏隐藏掉
    public void setStatusBarVisibility(Boolean status) {
        if (yfapi != null) {
            Logc.i("setStatusBarVisibility " + status);
            yfapi.yfsetStatusBarVisibility(status);
        }
    }

    public void setSysTime(Boolean status) {
        Logc.i("setSysTime " + status + " context:" + context);
        if (context != null) {
            Intent intent = new Intent("com.android.yf_setautotime");
            intent.putExtra("state", status);// state值分别是true和false,true表示设置为自动获取时间，false表示关闭自动获取时间。
            context.sendBroadcast(intent);
            Logc.i("setSysTime " + status);
        }
    }

//    // 跳转语言和输入设备
//    Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
//    startActivity(intent);
//
//    // 跳转 语言选择界面 【多国语言选择】【API 11及以上】
//    Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS);
//    startActivity(intent);
//
//    // 跳转语言选择界面【仅有English 和 中文两种选择】
//    Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
//    startActivity(intent);

}
