package com.if1001.cin.dage.DAO

import android.arch.persistence.room.*
import com.if1001.cin.dage.model.WORKOUT_ID_ROW
import com.if1001.cin.dage.model.WORKOUT_TABLE_NAME
import com.if1001.cin.dage.model.Workout

/**
 * Classe de acesso a dados (BD) de workouts
 */
@Dao
interface WorkoutDao {
    @Query("select * from $WORKOUT_TABLE_NAME")
    fun findWorkots(): List<Workout>

    @Query("select * from $WORKOUT_TABLE_NAME where $WORKOUT_ID_ROW = :id")
    fun findWorkoutByID(id: String): Workout

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWorkout(workout: Workout)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWorkout(workout: Workout)
}