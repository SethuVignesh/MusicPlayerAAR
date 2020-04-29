package com.example.musicplayer.playbackservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.musicplayer.R
import com.example.musicplayer.Song
import com.example.musicplayer.player.Controller
import java.util.*

class BackgroundAudioService : Service(), MediaPlayer.OnCompletionListener {
    internal var mediaPlayer: MediaPlayer? = null
    internal var mReceiver: BroadcastReceiver? = null
    internal var length = 0
    internal var current = 0
    internal var songList: ArrayList<Song>? = ArrayList()
    internal var state = Controller.STOP
    private var mRemoteViews: RemoteViews? = null
    internal var mNotificationManager: NotificationManager? = null
    internal val notificationId = "notify_001"
    internal var mBuilder = NotificationCompat.Builder(this, notificationId)



    inner class MyReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                "play" -> play(songList!![current])
                "pause" -> pausePlaying()
                "stop" -> mediaPlayer!!.release()
                "next" -> {
                }
                "resume" -> resumePlaying()
                "clear" -> clearNotification()
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        val filter = IntentFilter()
        filter.addAction("play")
        filter.addAction("pause")
        filter.addAction("stop")
        filter.addAction("next")
        filter.addAction("resume")
        mReceiver = MyReceiver()
        registerReceiver(mReceiver, filter)
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val songs = intent.getParcelableArrayListExtra<Song>("path")
        current = intent.getIntExtra("current", 0)
        startPlaying(songs)
        MUSIC_SERVICE_RUNNING = true
        return Service.START_STICKY
    }

    fun PushNotification(title: String, desc: String) {

        mRemoteViews = RemoteViews(packageName, R.layout.music_notification)

        mRemoteViews!!.setTextViewText(R.id.status_bar_track_name, title)
        mRemoteViews!!.setTextViewText(R.id.status_bar_artist_name, desc)

        val ii = Intent(this, MusicPlayerBroadcastLisener::class.java)
        ii.action = "com.example.musicplayer.ACTION_PLAYLIST"
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, ii, PendingIntent.FLAG_CANCEL_CURRENT)

        val playBtn = Intent(this, MusicPlayerBroadcastLisener::class.java)
        playBtn.action = "com.example.musicplayer.ACTION_PLAY"
        val pendingPlayIntent = PendingIntent.getBroadcast(this, 0, playBtn, 0)

        mRemoteViews!!.setOnClickPendingIntent(R.id.status_bar_play, pendingPlayIntent)

        val pauseBtn = Intent(this, MusicPlayerBroadcastLisener::class.java)
        pauseBtn.action = "com.example.musicplayer.ACTION_PAUSE"
        val pendingPauseSwitchIntent = PendingIntent.getBroadcast(this, 0, pauseBtn, 0)

        mRemoteViews!!.setOnClickPendingIntent(R.id.status_bar_pause, pendingPauseSwitchIntent)

        val stopBtn = Intent(this, MusicPlayerBroadcastLisener::class.java)
        stopBtn.action = "com.example.musicplayer.ACTION_STOP"
        val pendingStopSwitchIntent = PendingIntent.getBroadcast(this, 0, stopBtn, 0)

        mRemoteViews!!.setOnClickPendingIntent(R.id.status_bar_stop, pendingStopSwitchIntent)


        mBuilder.setContentIntent(pendingIntent)
        mBuilder.setSmallIcon(R.drawable.exo_icon_play)

        mBuilder.setContent(mRemoteViews)


        mNotificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "mushic_channel_id"
            val channel = NotificationChannel(
                channelId,
                "Thales Music Player",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null)
            mNotificationManager?.createNotificationChannel(channel)
            mBuilder.setChannelId(channelId)
        }

        mNotificationManager?.notify(121, mBuilder.build())

    }

    fun clearNotification() {
        mNotificationManager?.cancelAll()
    }

    override fun onDestroy() {
        MUSIC_SERVICE_RUNNING = false
        stopPlaying()
        unregisterReceiver(mReceiver)
    }

    override fun onCompletion(_mediaPlayer: MediaPlayer) {
        state = Controller.STOP
        MUSIC_SERVICE_RUNNING = false
    }

    fun startPlaying(path: ArrayList<Song>?) {
        songList = path
        play(songList!![current])
    }

    fun play(song: Song) {
        if (state === Controller.PAUSE) {
            resumePlaying()
        } else {
            stopPlaying()
            mediaPlayer = MediaPlayer.create(this, Uri.parse(song.path))
            mediaPlayer!!.setOnCompletionListener(this)
            mediaPlayer!!.start()
            PushNotification(song.title, song.artist)
            state = Controller.PLAY
        }
    }

    fun stopPlaying() {
        try {
            current = 0
            if (mediaPlayer == null) return
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
            }
            mediaPlayer!!.release()
            state = Controller.STOP
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }

    }

    fun pausePlaying() {
        if (mediaPlayer == null) return
        mediaPlayer!!.pause()
        length = mediaPlayer!!.currentPosition
        state = Controller.PAUSE
    }

    fun resumePlaying() {
        if (mediaPlayer == null) {
            startPlaying(songList)

        } else {
            mediaPlayer!!.seekTo(length)
            mediaPlayer!!.start()
            state = Controller.RESUME
        }
    }

    companion object {
        var MUSIC_SERVICE_RUNNING = false
    }


}
