package com.example.musicplayer.player

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musicplayer.Song
import com.example.musicplayer.playbackservice.BackgroundAudioService


class MusicPlayer(context: Context) {
    private var mp: MediaPlayer? = null
    private var context: Context = context
    private var length: Int = 0
    private var playbackServiceIntent: Intent? = null
    private val intent = Intent()

    init {
        playbackServiceIntent = Intent(context, BackgroundAudioService::class.java)

    }

    fun loadSongs(): ArrayList<Song> {
        return SongUtils.getLocalSongs(context)
    }

    public fun play(path: ArrayList<Song>?, pos: Int?) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    0
                )
            }
        } else {
            playbackServiceIntent?.putExtra("path", path)
            playbackServiceIntent?.putExtra("current", pos)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(playbackServiceIntent)
            } else {
                context.startService(playbackServiceIntent)
            }

        }
    }

    fun pause() {
        intent.action = "pause"
        context.sendBroadcast(intent)
    }

    fun stop() {
        intent.action = "stop"
        context.sendBroadcast(intent)
    }

    fun resume() {
        if (!BackgroundAudioService.MUSIC_SERVICE_RUNNING) {
            play(loadSongs(), 0)
        } else {
            intent.action = "resume"
            context.sendBroadcast(intent)
        }
    }

    fun next() {
        intent.action = "next"
        context.sendBroadcast(intent)
    }

    fun clear() {
        intent.action = "clear"
        context.sendBroadcast(intent)
    }


    fun stopPlaying() {

        intent.action = "stop"
        context.sendBroadcast(intent)
    }
}