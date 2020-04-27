package com.example.musicplayer.playbackservice;

import android.app.Notification;
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

import androidx.core.app.NotificationCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.Song;
import com.example.musicplayer.player.Controller;
import com.example.musicplayer.player.MusicPlayer;

import java.util.ArrayList;
import java.util.List;

public class BackgroundAudioService extends Service implements MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    BroadcastReceiver mReceiver;
    int length = 0;
    int current = 0;
    List<Song> songList = new ArrayList<>();
    Controller state = Controller.STOP;

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


        return START_STICKY;
    }

    NotificationManager mNotificationManager;

    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this, "notify_001");

    public void PushNotification(String title, String desc) {

        Intent ii = new Intent(this, MusicPlayer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(title);
        bigText.setBigContentTitle(desc);
        bigText.setSummaryText(desc);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.exo_icon_play);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "mushic_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Thales Music Player",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(121, mBuilder.build());

    }

    public void onDestroy() {
        stopPlaying();

        unregisterReceiver(mReceiver);
    }

    public void onCompletion(MediaPlayer _mediaPlayer) {

        state = Controller.STOP;
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
        if (mediaPlayer == null) return;
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
        state = Controller.RESUME;
    }

}
