package com.example.thales.musicplayer.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.thales.R
import com.example.thales.musicplayer.viewmodel.MusicPlayerViewModel


class MusicPlayerActivity : AppCompatActivity() {

    private var mAdapter: MyAdapter? = null
    private var viewModel: MusicPlayerViewModel? = null
    var musicController: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback)

    }

    fun pauseMusic(view: View) {
        viewModel?.pause()
    }

    fun playMusic(view: View) {
        viewModel?.play()
    }

    fun stopMusic(view: View) {
        viewModel?.stop()
        musicController?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel = MusicPlayerViewModel(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    0
                )
            }
        } else {
            viewModel?.loadLocalSongs()
            mAdapter = MyAdapter(emptyList(), viewModel)
            viewModel?.songList?.observe(this, Observer {
                mAdapter?.update(it)
            })
            val rv: RecyclerView = findViewById(R.id.rv_queue)
            rv.layoutManager = LinearLayoutManager(this)
            rv.adapter = mAdapter
        }
        musicController = findViewById(R.id.mpcontroller)
        viewModel?.playMode?.observe(this, Observer {
            if (it) {
                musicController?.visibility = View.VISIBLE
            } else {
                musicController?.visibility = View.GONE
            }
        }


        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 0)
            return
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel?.loadLocalSongs()
        }
    }


}
