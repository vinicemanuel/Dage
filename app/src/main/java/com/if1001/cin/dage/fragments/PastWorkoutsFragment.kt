package com.if1001.cin.dage.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.if1001.cin.dage.AppDatabase
import com.if1001.cin.dage.R
import com.if1001.cin.dage.adapters.ContentShareWorkout
import com.if1001.cin.dage.adapters.PastWorkoutsAdapter
import com.if1001.cin.dage.model.Workout
import kotlinx.android.synthetic.main.fragment_past_workouts.view.*


class PastWorkoutsFragment : Fragment(), ContentShareWorkout{

    private lateinit var myView: View
    private lateinit var recyclerView: RecyclerView
    private var workouts: List<Workout> = listOf<Workout>()

    override fun onItemClicked(workout: Workout) {
        Log.d("share", "compartilhado via Dage")

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Tocando: ${workout.playListName} \n\n Em: ${workout.locationName} \n\n (Compartilhado via Dage)")
        startActivity(shareIntent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        this.myView = inflater.inflate(R.layout.fragment_past_workouts, container, false)

        this.workouts = AppDatabase.getInstance(context!!).WorkoutDao().findWorkots().asReversed()

        Log.d("saved Workouts:", "${this.workouts.size}")

        this.loadRecycle()

        return this.myView
    }

    fun loadRecycle(){
        this.recyclerView = this.myView.past_workouts_recycleView
        this.recyclerView.adapter = PastWorkoutsAdapter(this.workouts, this.activity!!, this)
        val layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        this.recyclerView.layoutManager = layoutManager
    }

}
