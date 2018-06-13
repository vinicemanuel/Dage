package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import android.graphics.PointF
import com.if1001.cin.dage.ListPointConverter

const val WORKOUT_TABLE_NAME = "workout"
const val WORKOUT_NAME_ROW = "name"
const val WORKOUT_ID_ROW = "id"
const val WORKOUT_ROUTE = "route"

@Entity(tableName = WORKOUT_TABLE_NAME)
data class Workout(@ColumnInfo(name = WORKOUT_NAME_ROW) var locationName: String,
                   @ColumnInfo(name = WORKOUT_ROUTE) @TypeConverters(ListPointConverter::class) var route: String,
                   @ColumnInfo(name = WORKOUT_ID_ROW) @PrimaryKey var id: String) {
}