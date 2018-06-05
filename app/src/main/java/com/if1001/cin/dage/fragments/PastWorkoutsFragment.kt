package com.if1001.cin.dage.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.if1001.cin.dage.R
import com.if1001.cin.dage.model.Workout
import com.if1001.cin.dage.adapters.PastWorkoutsAdapter
import kotlinx.android.synthetic.main.fragment_past_workouts.view.*


class PastWorkoutsFragment : Fragment() {

    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var workouts: List<Workout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_past_workouts, container, false)
        this.recyclerView = this.myView.past_workouts_recycleView

        this.workouts = listOf(Workout("local 1"), Workout("local 2"), Workout("local 3"))

        this.recyclerView.adapter = PastWorkoutsAdapter(this.workouts, this.activity!!)
        val layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
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
