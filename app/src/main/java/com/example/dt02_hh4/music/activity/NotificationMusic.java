package com.example.dt02_hh4.music.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dt02_hh4.music.R;
import com.example.dt02_hh4.music.activity.adapter.MusicAdapter;
import com.example.dt02_hh4.music.model.Music;

public class NotificationMusic extends Service {
    private static final int NOTIFY_ID = -1;
    private MusicReceiver musicReceiver;
    private Music music;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getExtras()!=null){
           // music = (Music) intent.getExtras().getSerializable(MainActivity.MESSAGE_MUSIC);
        }
        Intent notIntent = new Intent(this, PlayingActivity.class);
        notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setTicker(music.getName())
                .setOngoing(true)
                .setContentTitle("Playing")
                .setContentText(music.getName());
        Notification not = builder.build();

        startForeground(NOTIFY_ID, not);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    private class MusicReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getExtras()!=null){
               // Music music = (Music) intent.getExtras().getSerializable(MainActivity.MESSAGE_MUSIC);
                Toast.makeText(NotificationMusic.this, music.getName(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
