package com.if1001.cin.dage.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.if1001.cin.dage.R
import com.if1001.cin.dage.model.Music
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cell_play_lists.view.*

interface ContentListenerMusic {
    fun onItemClicked(item: Music)
}

/**
 * Adapter para listagem de músicas em uma playlist
 */
class MusicAdapter(private val musics: List<Music>, private val act: Activity, val listener: ContentListenerMusic) : RecyclerView.Adapter<MusicAdapter.MusicHolder>() {
    class MusicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var musicName: TextView
        lateinit var musicDesc: TextView
        lateinit var musicImage: ImageView
        lateinit var music: Music

        fun bind(music: Music, listener: ContentListenerMusic) {
            this.music = music
            itemView.setOnClickListener {
                listener.onItemClicked(this.music)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicAdapter.MusicHolder {
        val myView = this.act.layoutInflater.inflate(R.layout.cell_play_lists, parent, false)
        val holder = MusicAdapter.MusicHolder(myView)
        holder.musicImage = myView.image_play_list
        holder.musicName = myView.playListName
        holder.musicDesc = myView.playListInfo

        return holder
    }

    override fun getItemCount(): Int {
        return this.musics.count()
    }

    override fun onBindViewHolder(holder: MusicAdapter.MusicHolder, position: Int) {
        holder.musicName.text = musics[position].MusicName
        holder.musicDesc.text = "${musics[position].Artist} - ${musics[position].Album} "
        Picasso.get().load(musics[position].ImageUrl).into(holder.musicImage)

        holder.bind(this.musics[position], this.listener)
    }
}