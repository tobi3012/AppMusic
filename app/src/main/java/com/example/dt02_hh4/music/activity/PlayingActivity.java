package com.example.dt02_hh4.music.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dt02_hh4.music.R;
import com.example.dt02_hh4.music.model.Music;
import com.example.dt02_hh4.music.receiver.CancelNotiListener;
import com.example.dt02_hh4.music.receiver.PlayPauseNotiListener;
import com.example.dt02_hh4.music.service.MusicService;

import java.io.IOException;

public class PlayingActivity extends AppCompatActivity {
    public static final String PLAY_PAUSE_KEY = "PLAY_PAUSE_KEY";
    public static MediaPlayer mp;
    private TextView tvNameSong;
    private TextView tvNameSinger;
    private ImageView ivPlay;
    public static boolean isPlaying;
    private int length;
    private PlayPauseNotiListener playPauseNotiListener;
    private CancelNotiListener cancelNotiListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        initView();
        playPauseNotiListener = new PlayPauseNotiListener();
        cancelNotiListener = new CancelNotiListener();
        IntentFilter intentFilterPlayPauseNoti = new IntentFilter(MusicService.ACTION_PLAY_NOTI);
        IntentFilter intentFilterCancelNoti = new IntentFilter(MusicService.ACTION_CANCEL_NOTI);
        registerReceiver(playPauseNotiListener, intentFilterPlayPauseNoti);
        registerReceiver(cancelNotiListener, intentFilterCancelNoti);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            Music music = (Music) bundle.getSerializable(MainActivity.MUSIC_PLAYING);
            if (music != null) {
                tvNameSong.setText(music.getName());
                tvNameSinger.setText(music.getSinger());
                playMusic(music.getPath());
                isPlaying = true;
            }
        }
        if (mp.isPlaying()) {
            isPlaying = true;
            ivPlay.setImageDrawable(getDrawable(R.drawable.pause_button));
        } else {
            isPlaying = false;
            ivPlay.setImageDrawable(getDrawable(R.drawable.play_button));
        }

        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    ivPlay.setImageDrawable(getDrawable(R.drawable.play_button));
                    isPlaying = false;
                    mp.pause();
                    length = mp.getCurrentPosition();
                } else {
                    ivPlay.setImageDrawable(getDrawable(R.drawable.pause_button));
                    isPlaying = true;
                    mp.seekTo(length);
                    mp.start();
                }
                sendToService(isPlaying);
            }
        });
    }

    private void initView() {
        tvNameSong = findViewById(R.id.tv_name_song);
        tvNameSinger = findViewById(R.id.tv_name_singer);
        ivPlay = findViewById(R.id.iv_play_pause);
    }

    private void playMusic(String path) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        mp = new MediaPlayer();
        try {
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playPauseNotiListener);
        unregisterReceiver(cancelNotiListener);
    }

    private void sendToService(boolean isPlay) {
        Intent intent = new Intent(PlayingActivity.this, MusicService.class);
        intent.putExtra(PLAY_PAUSE_KEY, isPlay);
        startService(intent);
    }
}
