package com.android.marinmusicplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SongAdapter(context: Context, items: ArrayList<Song>) : ArrayAdapter<Song>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_song, parent, false)
        var MusicName: TextView = itemView.findViewById(R.id.MusicName)
        var ArtistName: TextView = itemView.findViewById(R.id.ArtistName)
        val song = getItem(position)
        song?.let{
            MusicName.text = it.title
            ArtistName.text = it.artist
        }

        return itemView
    }
}
