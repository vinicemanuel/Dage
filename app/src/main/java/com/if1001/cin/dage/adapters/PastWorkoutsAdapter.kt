package com.if1001.cin.dage.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.if1001.cin.dage.R
import com.if1001.cin.dage.Workout
import kotlinx.android.synthetic.main.cell_past_workouts.view.*

class PastWorkoutsAdapter (private val workouts: List<Workout>, private val act: Activity): RecyclerView.Adapter<PastWorkoutsAdapter.PastWorkoutsHolder>() {

    class PastWorkoutsHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var textLocation: TextView
        lateinit var textPlayListName: TextView
        lateinit var imageView: ImageView
        lateinit var button: Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastWorkoutsHolder {
        val view = this.act.layoutInflater.inflate(R.layout.cell_past_workouts,parent, false)
        val holder = PastWorkoutsHolder(view)
        holder.button = view.shareButton
        holder.textLocation = view.textLocation
        holder.imageView = view.imageView
        holder.textPlayListName = view.textPlayListName

        holder.button.setOnClickListener { Log.d("cick: ", "compartilhado via Dage") }
        return holder
    }

    override fun getItemCount(): Int {
        return this.workouts.size
    }

    override fun onBindViewHolder(holder: PastWorkoutsHolder, position: Int) {
        holder.textLocation.text = "$position"
    }
}