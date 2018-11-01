package com.example.dt02_hh4.music.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.dt02_hh4.music.R;
import com.example.dt02_hh4.music.activity.MainActivity;
import com.example.dt02_hh4.music.activity.PlayingActivity;
import com.example.dt02_hh4.music.model.Music;

import static com.example.dt02_hh4.music.app.MyApp.CHANNEL_ID;

public class MusicService extends Service {
    public static final String ACTION_PLAY_NOTI = "ACTION_PLAY_NOTI";
    public static final String ACTION_CANCEL_NOTI = "ACTION_CANCEL_NOTI";
    public static final String IS_PLAYING = "IS_PLAYING";
    public static RemoteViews notificationLayout;
    Music music;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationLayout = new RemoteViews(getPackageName(), R.layout.activity_notification_music);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {
            music = (Music) intent.getExtras().getSerializable(MainActivity.MUSIC_SERVICE);
            boolean isPlaying = intent.getExtras().getBoolean(PlayingActivity.PLAY_PAUSE_KEY);
            if (music != null) {
                notificationLayout.setTextViewText(R.id.tv_name_song_noti, music.getName());
                notificationLayout.setTextViewText(R.id.tv_name_singer_noti, music.getSinger());
                isPlaying = true;
            }
            if(isPlaying){
                notificationLayout.setImageViewResource(R.id.iv_play_pause_noti, R.drawable.pause_button_16);
            } else {
                notificationLayout.setImageViewResource(R.id.iv_play_pause_noti, R.drawable.play_button_16);
            }
            startServiceWithNotification(isPlaying);
        } else {
            stopService();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void startServiceWithNotification(boolean isPlaying) {
        Intent notificationIntent = new Intent(this, PlayingActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intentPlayNoti = new Intent(ACTION_PLAY_NOTI);
        intentPlayNoti.putExtra(IS_PLAYING, isPlaying);
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 0,
                intentPlayNoti, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationLayout.setOnClickPendingIntent(R.id.iv_play_pause_noti, pendingPlayIntent);


        Intent intentCancelNoti = new Intent(ACTION_CANCEL_NOTI);
        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(this, 0,
                intentCancelNoti, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationLayout.setOnClickPendingIntent(R.id.iv_cancel_noti, pendingCancelIntent);

        Notification customNotification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.music_player_icon)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, customNotification);
    }

    public void stopService() {
        stopForeground(true);
        stopSelf();
    }

}
