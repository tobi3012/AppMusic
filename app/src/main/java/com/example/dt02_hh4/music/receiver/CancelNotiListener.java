package com.example.dt02_hh4.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CancelNotiListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "dang hat", Toast.LENGTH_SHORT).show();

    }
}
