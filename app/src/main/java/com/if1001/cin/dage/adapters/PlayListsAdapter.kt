package com.if1001.cin.dage.adapters

import android.app.Activity
import android.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.if1001.cin.dage.HOME_FRAGMENT_TAG
import com.if1001.cin.dage.PLAYING_SONG_FRAGMENT_TAG
import com.if1001.cin.dage.R
import com.if1001.cin.dage.fragments.MapPlaylistFragment
import com.if1001.cin.dage.fragments.PlayingFragment
import com.if1001.cin.dage.model.PlayList
import kotlinx.android.synthetic.main.cell_play_lists.view.*

public interface ContentListenerPlayList {
    fun onItemClicked(item: PlayList)
}

class PlayListsAdapter (private val playLists: List<PlayList>, private val act: Activity, val listener: ContentListenerPlayList): RecyclerView.Adapter<PlayListsAdapter.PlayListsHolder>() {

    class PlayListsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var playListName: TextView
        lateinit var playListdesc: TextView
        lateinit var plalistImage: ImageView
        lateinit var playList: PlayList

        fun bind(playList: PlayList, listener: ContentListenerPlayList){
            this.playList = playList
            itemView.setOnClickListener {
                listener.onItemClicked(this.playList)
            }
        }
    }

    val playingFragment: PlayingFragment = PlayingFragment()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListsHolder {
        val myView = this.act.layoutInflater.inflate(R.layout.cell_play_lists, parent, false)
        val holder = PlayListsHolder(myView)
        holder.plalistImage = myView.image_play_list
        holder.playListName = myView.playListName
        holder.playListdesc = myView.playListInfo

        return holder
    }

    override fun getItemCount(): Int {
        return this.playLists.count()
    }

    override fun onBindViewHolder(holder: PlayListsHolder, position: Int) {
        holder.playListName.text = playLists[position].PlayListName
        holder.playListdesc.text = playLists[position].PlayListName

        holder.bind(this.playLists[position],this.listener)

    }
}