package com.if1001.cin.dage.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.if1001.cin.dage.R
import com.if1001.cin.dage.model.PlayList
import kotlinx.android.synthetic.main.cell_play_lists.view.*

class PlayListsAdapter (private val playLists: List<PlayList>, private val act: Activity): RecyclerView.Adapter<PlayListsAdapter.PlayListsHolder>() {

    class PlayListsHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var playListName: TextView
        lateinit var playListdesc: TextView
        lateinit var plalistImage: ImageView
    }

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
    }
}