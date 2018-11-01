package com.example.dt02_hh4.music.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.dt02_hh4.music.R;
import com.example.dt02_hh4.music.activity.adapter.MusicAdapter;
import com.example.dt02_hh4.music.model.Music;
import com.example.dt02_hh4.music.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String MUSIC_PLAYING = "MUSIC_PLAYING";
    public static final String MUSIC_SERVICE = "MUSIC_SERVICE";
    private static final int MY_PERMISSION_REQUEST = 1;
    private RecyclerView musicRecyclerView;
    private List<Music> musicArrayList = new ArrayList<>();
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);

            }
        } else {
            doStuff();
        }
    }

    private void doStuff() {
        initView();
        getAllMediaMp3Files();
        MusicAdapter musicAdapter = new MusicAdapter(musicArrayList, new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*Intent intent = new Intent(MainActivity.this, PlayingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MUSIC_PLAYING, musicArrayList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);*/
                sendToService(position);
                goToPlayingActivity(position);
            }
        }, this);
        musicRecyclerView.setAdapter(musicAdapter);
    }

    private void initView() {
        musicRecyclerView = findViewById(R.id.rv_líst_music);
        RecyclerView.LayoutManager musicLayoutManager = new LinearLayoutManager(getApplicationContext());
        musicRecyclerView.setLayoutManager(musicLayoutManager);
    }

    public void getAllMediaMp3Files() {
        ContentResolver contentResolver = this.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        try (Cursor cursor = contentResolver.query(
                uri,
                null,
                null,
                null,
                null
        )) {
            if (cursor == null) {
                Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG).show();
            } else if (!cursor.moveToFirst()) {
                Toast.makeText(MainActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG).show();
            } else {
                int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int Artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int Path = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                do {
                    String SongTitle = cursor.getString(Title);
                    String SongArtist = cursor.getString(Artist);
                    String SongPath = cursor.getString(Path);

                    Music music = new Music(SongTitle, null, SongArtist, SongPath);
                    musicArrayList.add(music);
                } while (cursor.moveToNext());
            }
        }
    }

    private void sendToService(int position) {
        intent = new Intent(MainActivity.this, MusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MUSIC_SERVICE, musicArrayList.get(position));
        intent.putExtras(bundle);
        if (intent != null) {
            startService(intent);
        }
    }

    private void goToPlayingActivity(int position) {
        intent = new Intent(MainActivity.this, PlayingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MUSIC_PLAYING, musicArrayList.get(position));
        intent.putExtras(bundle);
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(serviceIntent);
    }
}
