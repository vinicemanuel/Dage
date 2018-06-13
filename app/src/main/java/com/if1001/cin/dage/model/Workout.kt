package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.graphics.PointF

const val WORKOUT_TABLE_NAME = "workout"
const val WORKOUT_NAME_ROW = "name"
const val WORKOUT_ID_ROW = "id"
const val WORKOUT_ROUTE = "route"

@Entity(tableName = WORKOUT_TABLE_NAME)
data class Workout(@ColumnInfo(name = WORKOUT_NAME_ROW) var locationName: String,
                   var route: List<PointF>,
                   @ColumnInfo(name = WORKOUT_ID_ROW) @PrimaryKey var id: String) {
}