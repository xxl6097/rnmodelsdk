package com.android.api.util;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

/**
 * https://blog.csdn.net/yechaoa/article/details/112248013
 * STREAM_VOICE_CALL 通话
 * STREAM_SYSTEM 系统
 * STREAM_RING 铃声
 * STREAM_MUSIC 媒体音量
 * STREAM_ALARM 闹钟
 * STREAM_NOTIFICATION 通知
 *
 *
 * 音量模式：
 *
 * RINGER_MODE_NORMAL 正常
 * RINGER_MODE_SILENT 静音
 * RINGER_MODE_VIBRATE 震动
 */
public class VolumeManager {
    private AudioManager audioManager;

    VolumeManager() {
    }

    private volatile static VolumeManager instance = null;

    public static VolumeManager getInstance() {
        if (instance == null) {
            synchronized (VolumeManager.class) {
                if (instance == null) {
                    instance = new VolumeManager();
                }
            }
        }
        return instance;
    }

    private Context mContext;
    private int mMax;
    private int mCurrVolume;
    private SettingsContentObserver Content;

    public void init(Context context, Handler handler) {
        mContext = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mCurrVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Content = new SettingsContentObserver(mContext, handler);
        registerVolumeChangeReceiver();


        /**
         *    STREAM_VOICE_CALL 通话
         *  * STREAM_SYSTEM 系统
         *  * STREAM_RING 铃声
         *  * STREAM_MUSIC 媒体音量
         *  * STREAM_ALARM 闹钟
         *  * STREAM_NOTIFICATION 通知
         */
        int max_call = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int max_sys = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int max_ring = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        int max_music = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int max_alrlm = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        int max_motify = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        Logc.i("======通话:"+max_call+" 系统:"+max_sys+" 铃声:"+max_ring+" 媒体:"+max_music+" 闹钟:"+max_alrlm+" 通知:"+max_motify);
    }

    public int getCurrVolume() {
        int pos = mCurrVolume* 100/ mMax ;
        return pos;
    }

    public int getMusicVolume() {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int pos = vol * 100/ max ;
        return pos;
    }


    public void setVolumeMusic(int volumeLevel) {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float progress = volumeLevel / 100.0f;
        float fvalue = progress * max;
        int value = max * volumeLevel / 100;
        if(fvalue > 0 && fvalue < 1){
            value = 1;
        }
        Logc.i("====setVolumeMusic max:"+max+" volumeLevel:"+volumeLevel+",value:"+value+",fvalue:"+fvalue);
        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

    }

    public int getRingVolume() {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int pos = vol * 100/ max ;
        return pos;
    }
    public void setVolumeRing(int volumeLevel) {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        float progress = volumeLevel / 100.0f;
        float fvalue = progress * max;
        int value = max * volumeLevel / 100;
        if(fvalue > 0 && fvalue < 1){
            value = 1;
        }
        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_RING, value, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }


    public int getAlarmVolume() {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        int pos = vol * 100/ max ;
        return pos;
    }
    public void setVolumeAlarm(int volumeLevel) {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        float progress = volumeLevel / 100.0f;
        float fvalue = progress * max;
        int value = max * volumeLevel / 100;
        if(fvalue > 0 && fvalue < 1){
            value = 1;
        }
        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, value, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public int getSystemMaxVolume() {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        return max;
    }

    public int getSystemVolume() {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        int vol = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int pos = vol * 100/ max ;
        Logc.i("====getSystemVolume max:"+max+" vol:"+vol+",pos:"+pos);
        return pos;
    }
    public void setVolumeSystem(int volumeLevel) {
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        float progress = volumeLevel / 100.0f;
        float fvalue = progress * max;
        int value = max * volumeLevel / 100;
        if(fvalue > 0 && fvalue < 1){
            value = 1;
        }
        Logc.i("====setVolumeSystem max:"+max+" volumeLevel:"+volumeLevel+",value:"+value+",fvalue:"+fvalue);
//        if(audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//        }
        //audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, value, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, value, AudioManager.FLAG_SHOW_UI);
    }

    public void ringVolumeUp() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
    }
    public void ringVolumeDown() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
    }

    public void alarmVolumeUp() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
    }
    public void alarmVolumeDown() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
    }

    public void systemVolumeUp() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
    }
    public void systemVolumeDown() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
    }

    public void musicVolumeUp() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI);
    }
    public void musicVolumeDown() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI);
    }


    public class SettingsContentObserver extends ContentObserver {
        Context context;

        public SettingsContentObserver(Context c, Handler handler) {
            super(handler);
            context = c;
        }

        @Override
        public boolean deliverSelfNotifications() {
            return super.deliverSelfNotifications();
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mCurrVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            notifyVolumeChange(mMax, mCurrVolume);
        }
    }


    public void notifyVolumeChange(int max, int progress) {
        if (listener != null) {
            listener.onVolumeChange(max, progress);
        }
    }

    public void registerVolumeChangeReceiver() {
        mContext.getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, Content);
    }

    public void unregisterRece() {
        mContext.getContentResolver().unregisterContentObserver(Content);
    }

    private VolumeChangeListener listener;

    public interface VolumeChangeListener {
        void onVolumeChange(int max, int progress);

    }

    public void registerVolumeListener(VolumeChangeListener VolumeChangeListener) {
        listener = VolumeChangeListener;
        notifyVolumeChange(mMax, mCurrVolume);
    }
}
