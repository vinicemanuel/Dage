package com.if1001.cin.dage.adapters

import android.app.Activity
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.if1001.cin.dage.R
import com.if1001.cin.dage.model.Workout
import kotlinx.android.synthetic.main.cell_past_workouts.view.*

interface ContentShareWorkout {
    fun onItemClicked(workout: Workout)
}

/**
 * Adapter personalizado para lista de Workouts
 */
class PastWorkoutsAdapter(private val workouts: List<Workout>, private val act: Activity, val listener: ContentShareWorkout) : RecyclerView.Adapter<PastWorkoutsAdapter.PastWorkoutsHolder>() {
    class PastWorkoutsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var textLocation: TextView
        lateinit var textPlayListName: TextView
        lateinit var imageView: ImageView
        lateinit var button: Button

        fun bind(workout: Workout, listener: ContentShareWorkout) {
            button.setOnClickListener {
                listener.onItemClicked(workout)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastWorkoutsHolder {
        val view = this.act.layoutInflater.inflate(R.layout.cell_past_workouts, parent, false)
        val holder = PastWorkoutsHolder(view)
        holder.button = view.shareButton
        holder.textLocation = view.textLocation
        holder.imageView = view.cell_imageView
        holder.textPlayListName = view.textPlayListName

        return holder
    }

    override fun getItemCount(): Int {
        return this.workouts.size
    }

    /**
     * Bind de dados com UI
     */
    override fun onBindViewHolder(holder: PastWorkoutsHolder, position: Int) {
        holder.textLocation.text = this.workouts[position].locationName
        holder.textPlayListName.text = this.workouts[position].playListName

        val imageBytes = Base64.decode(this.workouts[position].image, 0)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        holder.bind(workouts[position], this.listener)
        holder.imageView.setImageBitmap(image)
    }
}