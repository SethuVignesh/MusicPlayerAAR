package com.example.thales.musicplayer.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicplayer.Song
import com.example.musicplayer.player.Controller
import com.example.musicplayer.player.MusicPlayer
import com.example.thales.SongUtils


class MusicPlayerViewModel(
    private val context: Context


) : ViewModel(), LifecycleObserver {
    private val musicPlayer: MusicPlayer = MusicPlayer(context)
    val playMode: MutableLiveData<Boolean> = MutableLiveData()
    val songList: MutableLiveData<ArrayList<Song>> = MutableLiveData()
    val currentSongPos: MutableLiveData<Int> = MutableLiveData()

    class Factory(
        private val context: Context
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return MusicPlayerViewModel(context) as T
        }
    }

    fun control(controller: Controller, path: ArrayList<Song>?, pos: Int?) {
        when (controller) {
            Controller.PLAY -> {
                musicPlayer.play(path, pos)
            }
            Controller.PAUSE -> {
                musicPlayer.pause()
            }
            Controller.RESUME -> {
                musicPlayer.resume()
            }
            Controller.NEXT -> {

            }
            Controller.PREV -> {
            }
            Controller.STOP -> {
                musicPlayer.stop()
            }
        }

    }

    fun loadLocalSongs() {
        val songs = SongUtils.getLocalSongs(context)
        songList.value = songs

    }

    fun play() {
        control(Controller.PLAY, songList.value, currentSongPos.value)
    }

    fun pause() {
        control(Controller.PAUSE, null, null)
    }

    fun stop() {
        control(Controller.STOP, null, null)
    }

    fun resume() {
        control(Controller.RESUME, null, null)
    }


}