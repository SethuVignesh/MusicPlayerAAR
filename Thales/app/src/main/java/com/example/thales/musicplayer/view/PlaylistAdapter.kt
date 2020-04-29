package com.example.thales.musicplayer.view;

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Song
import com.example.musicplayer.player.Controller
import com.example.thales.R
import com.example.thales.musicplayer.viewmodel.MusicPlayerViewModel
import java.util.*

class PlaylistAdapter(it: List<Song>, val viewmodel: MusicPlayerViewModel?) :
    RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>() {

    private val rollOverListItem = ArrayList<Song>()
    private var selectedItem = -1;

    init {
        if (it != null)
            this.rollOverListItem.addAll(it)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvTitle: TextView
        var list: LinearLayout
        var row: LinearLayout

        init {
            tvTitle = view.findViewById(R.id.tv_item_song_title) as TextView
            list = view.findViewById(R.id.list_item) as LinearLayout
            row = view.findViewById(R.id.row) as LinearLayout

        }
    }

    fun update(it: List<Song>) {
        rollOverListItem.clear()
        rollOverListItem.addAll(it)
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvTitle.text = rollOverListItem[position].title
        holder.list.setOnClickListener({
            viewmodel?.control(Controller.PLAY, rollOverListItem, position)
            viewmodel?.playMode?.value = true
            viewmodel?.currentSongPos?.value = position
            selectedItem = position
            notifyDataSetChanged()
        })
        if (position == selectedItem) {
            holder.row.setBackgroundColor(Color.BLUE)
        } else {
            holder.row.setBackgroundColor(Color.parseColor("#424242"))
        }

    }


    override fun getItemCount(): Int {
        return rollOverListItem.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_item, parent, false)
        return MyViewHolder(v)
    }
}