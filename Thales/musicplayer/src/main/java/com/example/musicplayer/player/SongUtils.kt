package com.example.musicplayer.player

import android.content.Context
import android.provider.MediaStore
import android.text.TextUtils
import com.example.musicplayer.Song
import java.util.*

object SongUtils {

    fun getLocalSongs(context: Context): ArrayList<Song> {
        val list = ArrayList<Song>()

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            MediaStore.Audio.AudioColumns.IS_MUSIC
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val song = Song()
                song.id = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                song.title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                song.artist =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                song.path =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                song.duration =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))

                if (song.title.contains("-")) {
                    val str = song.title.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    if (TextUtils.isEmpty(song.artist))
                        song.artist = str[0]
                    if (TextUtils.isEmpty(song.title))
                        song.title = str[1]
                }
                list.add(song)
            }
            cursor.close()
        }

        return list
    }


}
