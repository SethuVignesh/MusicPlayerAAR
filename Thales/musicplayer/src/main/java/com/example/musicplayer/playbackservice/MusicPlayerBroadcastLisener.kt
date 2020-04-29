package com.example.musicplayer.playbackservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.example.musicplayer.player.MusicPlayer

class MusicPlayerBroadcastLisener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val musicPlayer = MusicPlayer(context)
        when (intent.action) {
            "com.example.musicplayer.ACTION_PLAY" -> musicPlayer.resume()
            "com.example.musicplayer.ACTION_PAUSE" -> musicPlayer.pause()
            "com.example.musicplayer.ACTION_STOP" -> musicPlayer.stop()
            "com.example.musicplayer.ACTION_CLOSE" -> {
                musicPlayer.stop()
                musicPlayer.clear()
            }
            "com.example.musicplayer.ACTION_PLAYLIST" -> {
                val i = Intent("your.custom.ACTION")
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(i)
            }
        }
    }
}