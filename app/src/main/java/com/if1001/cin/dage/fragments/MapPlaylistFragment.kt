package com.if1001.cin.dage.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.if1001.cin.dage.model.PlayList
import com.if1001.cin.dage.R
import com.if1001.cin.dage.adapters.PlayListsAdapter
import kotlinx.android.synthetic.main.fragment_map_playlist.view.*


class MapPlaylistFragment : Fragment() {

    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlists: List<PlayList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_map_playlist, container, false)
        this.recyclerView = this.myView.recycle_view_playlist
        this.playlists = listOf(PlayList("list 1"), PlayList("list 2"), PlayList("list 3"))

        this.recyclerView.adapter = PlayListsAdapter(this.playlists,this.activity!!)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        this.recyclerView.layoutManager = layoutManager

        return this.myView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

}
