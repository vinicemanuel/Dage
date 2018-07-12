package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.if1001.cin.dage.ListPointConverter

const val WORKOUT_TABLE_NAME = "workout"
const val WORKOUT_NAME_ROW = "name"
const val WORKOUT_ID_ROW = "id"
const val WORKOUT_ROUTE_ROW = "route"
const val WORKOUT_ROUTE_PLAY_LIST_NAME_ROW = "playListName"
const val WORKOUT_ROUTE_IMAGE_ROW = "image"

/**
 * Classe de de mapeamento com banco de dados (ORM) de workout
 */
@Entity(tableName = WORKOUT_TABLE_NAME)
data class Workout(@ColumnInfo(name = WORKOUT_NAME_ROW) var locationName: String,
                   @ColumnInfo(name = WORKOUT_ROUTE_ROW) @TypeConverters(ListPointConverter::class) var route: String,
                   @ColumnInfo(name = WORKOUT_ROUTE_PLAY_LIST_NAME_ROW) var playListName: String,
                   @ColumnInfo(name = WORKOUT_ROUTE_IMAGE_ROW) var image: String) {

    @ColumnInfo(name = WORKOUT_ID_ROW)
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}