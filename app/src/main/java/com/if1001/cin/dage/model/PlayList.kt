package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val PLAYLIST_TABLE_NAME = "playlist"
const val PLAYLIST_NAME_ROW = "name"
const val PLAYLIST_DESC_ROW = "description"
const val PLAYLIST_IMAGE_URL_ROW = "image_url"
const val PLAYLIST_ID_ROW = "id"

/**
 * Classe de de mapeamento com banco de dados (ORM) de playlists
 */
@Entity(tableName = PLAYLIST_TABLE_NAME)
data class PlayList(@ColumnInfo(name = PLAYLIST_NAME_ROW) var PlayListName: String,
                    @ColumnInfo(name = PLAYLIST_DESC_ROW) var Description: String,
                    @ColumnInfo(name = PLAYLIST_IMAGE_URL_ROW) var ImageUrl: String,
                    @ColumnInfo(name = PLAYLIST_ID_ROW) @PrimaryKey var id: String)