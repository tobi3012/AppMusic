package com.example.dt02_hh4.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.dt02_hh4.music.activity.PlayingActivity;
import com.example.dt02_hh4.music.service.MusicService;

public class PlayPauseNotiListener extends BroadcastReceiver {
    private int length;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            boolean isPlaying = intent.getBooleanExtra(MusicService.IS_PLAYING, true);
            if (isPlaying) {
                PlayingActivity.mp.pause();
                length = PlayingActivity.mp.getCurrentPosition();
                PlayingActivity.isPlaying = false;
            } else {
                PlayingActivity.mp.seekTo(length);
                PlayingActivity.mp.start();
                PlayingActivity.isPlaying = true;
            }
        }
    }
}
