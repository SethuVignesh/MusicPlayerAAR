package com.example.musicplayer.playbackservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.Song;
import com.example.musicplayer.player.Controller;

import java.util.ArrayList;

public class BackgroundAudioService extends Service implements MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    BroadcastReceiver mReceiver;
    int length = 0;
    int current = 0;
    ArrayList<Song> songList = new ArrayList<>();
    Controller state = Controller.STOP;
    private RemoteViews mRemoteViews;
    public static boolean MUSIC_SERVICE_RUNNING = false;

    // use this as an inner class like here or as a top-level class
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "play":
                    play(songList.get(current));
                    break;
                case "pause":
                    pausePlaying();
                    break;
                case "stop":
                    mediaPlayer.release();
                    break;
                case "next":
                    break;
                case "resume":
                    resumePlaying();
                    break;
                case "clear":
                    clearNotification();
                    break;

            }
        }

        // constructor
        public MyReceiver() {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("play");
        filter.addAction("pause");
        filter.addAction("stop");
        filter.addAction("next");
        filter.addAction("resume");
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, filter);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<Song> songs = intent.getParcelableArrayListExtra("path");
        current = intent.getIntExtra("current", 0);
        startPlaying(songs);

        MUSIC_SERVICE_RUNNING = true;
        return START_STICKY;
    }

    NotificationManager mNotificationManager;

    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this, "notify_001");

    public void PushNotification(String title, String desc) {

        mRemoteViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

        mRemoteViews.setTextViewText(R.id.status_bar_track_name, title);
        mRemoteViews.setTextViewText(R.id.status_bar_artist_name, desc);

        Intent ii = new Intent(this, MusicPlayerBroadcastLisener.class);
        ii.setAction("com.example.musicplayer.ACTION_PLAYLIST");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, ii, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent playBtn = new Intent(this, MusicPlayerBroadcastLisener.class);
        playBtn.setAction("com.example.musicplayer.ACTION_PLAY");
        PendingIntent pendingPlayIntent = PendingIntent.getBroadcast(this, 0, playBtn, 0);

        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_play, pendingPlayIntent);

        Intent pauseBtn = new Intent(this, MusicPlayerBroadcastLisener.class);
        pauseBtn.setAction("com.example.musicplayer.ACTION_PAUSE");
        PendingIntent pendingPauseSwitchIntent = PendingIntent.getBroadcast(this, 0, pauseBtn, 0);

        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_pause, pendingPauseSwitchIntent);

        Intent stopBtn = new Intent(this, MusicPlayerBroadcastLisener.class);
        stopBtn.setAction("com.example.musicplayer.ACTION_STOP");
        PendingIntent pendingStopSwitchIntent = PendingIntent.getBroadcast(this, 0, stopBtn, 0);

        mRemoteViews.setOnClickPendingIntent(R.id.status_bar_stop, pendingStopSwitchIntent);


        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.exo_icon_play);

        mBuilder.setContent(mRemoteViews);


        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "mushic_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thales Music Player",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(121, mBuilder.build());

    }

    public void clearNotification() {
        mNotificationManager.cancelAll();
    }

    public void onDestroy() {
        MUSIC_SERVICE_RUNNING = false;
        stopPlaying();
        unregisterReceiver(mReceiver);
    }

    public void onCompletion(MediaPlayer _mediaPlayer) {
        state = Controller.STOP;
        MUSIC_SERVICE_RUNNING = false;
    }

    public void startPlaying(ArrayList<Song> path) {
        songList = path;
        play(songList.get(current));
    }

    public void play(Song song) {
        if (state == Controller.PAUSE) {
            resumePlaying();
        } else {
            stopPlaying();
            mediaPlayer = MediaPlayer.create(this, Uri.parse(song.getPath()));
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
            PushNotification(song.getTitle(), song.getArtist());
//            setUpNotification();
            state = Controller.PLAY;
        }
    }

    public void stopPlaying() {
        try {
            current = 0;
            if (mediaPlayer == null) return;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            state = Controller.STOP;
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        }
    }

    public void pausePlaying() {
        if (mediaPlayer == null) return;
        mediaPlayer.pause();
        length = mediaPlayer.getCurrentPosition();
        state = Controller.PAUSE;
    }

    public void resumePlaying() {
        if (mediaPlayer == null) {
            startPlaying(songList);

        } else {
            mediaPlayer.seekTo(length);
            mediaPlayer.start();
            state = Controller.RESUME;
        }
    }


}
