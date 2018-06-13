package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val PLAYLIST_TABLE_NAME = "playlist"
const val PLAYLIST_NAME_ROW = "name"
const val PLAYLIST_ID_ROW = "id"

@Entity(tableName = PLAYLIST_TABLE_NAME)
data class PlayList(@ColumnInfo(name = PLAYLIST_NAME_ROW) var PlayListName: String,
                    @ColumnInfo(name = PLAYLIST_ID_ROW) @PrimaryKey var id: String) {
}