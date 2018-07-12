package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val MUSIC_TABLE_NAME = "music"
const val MUSIC_NAME_ROW = "name"
const val MUSIC_ARTIST_ROW = "artist"
const val MUSIC_ALBUM_ROW = "album"
const val MUSIC_IMAGE_URL_ROW = "image_url"
const val MUSIC_ID_ROW = "id"

/**
 * Classe de de mapeamento com banco de dados (ORM) de músicas
 */
@Entity(tableName = MUSIC_TABLE_NAME)
data class Music(@ColumnInfo(name = MUSIC_NAME_ROW) var MusicName: String,
                 @ColumnInfo(name = MUSIC_ARTIST_ROW) var Artist: String,
                 @ColumnInfo(name = MUSIC_ALBUM_ROW) var Album: String,
                 @ColumnInfo(name = MUSIC_IMAGE_URL_ROW) var ImageUrl: String,
                 @ColumnInfo(name = MUSIC_ID_ROW) @PrimaryKey var id: String)