package com.example.musicplayer.playbackservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.musicplayer.player.MusicPlayer;

public class MusicPlayerBroadcastLisener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //move to DI
        MusicPlayer musicPlayer = new MusicPlayer(context);
        switch (intent.getAction()) {
            case "com.example.musicplayer.ACTION_PLAY":
                musicPlayer.resume();
                break;
            case "com.example.musicplayer.ACTION_PAUSE":
                musicPlayer.pause();
                break;
            case "com.example.musicplayer.ACTION_STOP":
                musicPlayer.stop();
                break;
            case "com.example.musicplayer.ACTION_CLOSE":
                musicPlayer.stop();
                musicPlayer.clear();
                break;
            case "com.example.musicplayer.ACTION_PLAYLIST":
                Intent i = new Intent("your.custom.ACTION");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
        }
    }
}