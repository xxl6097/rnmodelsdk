package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.SeekBar;

import com.android.api.util.PermissionHelper;
import com.android.api.util.ScreenBrightnessApi;
import com.android.api.util.VolumeManager;

public class MainActivity extends Activity {
    private SeekBar bri,aodiu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VolumeManager.getInstance().init(this,new Handler());
        VolumeManager.getInstance().registerVolumeListener(new VolumeManager.VolumeChangeListener() {
            @Override
            public void onVolumeChange(int max, int progress) {
                System.out.println("====registerVolumeListener max:"+max+" progress:"+progress);
            }
        });
        ScreenBrightnessApi.getApi().allowModifySettings(this);
        setContentView(R.layout.activity_main);
        bri = findViewById(R.id.seekBar1);
        aodiu = findViewById(R.id.seekBar2);
        bri.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ScreenBrightnessApi.getApi().setScreenBrightness(MainActivity.this, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        int pos = ScreenBrightnessApi.getApi().getScreenBrightnessProgress(this);
        bri.setProgress(pos);


        //aodiu.setMax(VolumeManager.getInstance().getSystemMaxVolume());
        aodiu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                VolumeManager.getInstance().setVolumeMusic(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        aodiu.setProgress(VolumeManager.getInstance().getCurrVolume());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        ScreenBrightnessApi.getApi().onActivityResult(this,requestCode,resultCode,data);
    }


    public void onVolUp(View view) {
        VolumeManager.getInstance().systemVolumeUp();
        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));

    }

    public void onVolDown(View view) {
        VolumeManager.getInstance().systemVolumeDown();
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
    }

    public void onDeviceInfo(View view) {
        PermissionHelper.checkPermisson(this);
    }
}