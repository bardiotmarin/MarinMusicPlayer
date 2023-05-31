package com.android.marinmusicplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SongsAdapter(context: Context, items: ArrayList<Song>) : ArrayAdapter<Song>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        val musicName: TextView = itemView.findViewById(R.id.musicName)
        val artistName: TextView = itemView.findViewById(R.id.artistName)
        val song = getItem(position)
        song?.let{
            musicName.text = it.title
            artistName.text = it.artist
        }

        return itemView
    }
}
