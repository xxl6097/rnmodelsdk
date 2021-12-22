package com.android.api.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class MediaHelper {
    private Context context;
    private MediaPlayer player;
    private MediaPlayer.OnCompletionListener listener;

    public void init(Context context, MediaPlayer.OnCompletionListener listener) {
        this.context = context;
        player = new MediaPlayer();
        this.listener = listener;
    }

    public void setPlayerRes(int resId) {
        if (player == null) {
            player = new MediaPlayer();
        }
        if (player.isPlaying()) {
            player.stop();
        }
        AssetFileDescriptor file = context.getResources().openRawResourceFd(resId);
        try {
            player.reset();
            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                    file.getLength());
            player.prepare();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(listener);
        player.setLooping(false);
        player.start();
    }

    public void playAsUrl(final String url){
        if (player == null) {
            player = new MediaPlayer();
        }
        if (player.isPlaying()) {
            player.stop();
        }
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.reset();
            player.setDataSource(url);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnCompletionListener(listener);
        player.setLooping(false);
        player.start();
    }

    public void play() {
        if (player != null) {
            try {
                player.reset();
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.start();
        }
    }

    public boolean isPlaying() {
        try {
            if (player != null) {
                return player.isPlaying();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    public void release() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
