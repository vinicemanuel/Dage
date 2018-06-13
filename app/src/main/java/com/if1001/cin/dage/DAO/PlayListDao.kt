package com.if1001.cin.dage.DAO

import android.arch.persistence.room.*
import com.if1001.cin.dage.model.*

@Dao
interface PlayListDao {
    @Query("select * from $PLAYLIST_TABLE_NAME")
    fun findPlayLists(): List<PlayList>

    @Query("select * from $PLAYLIST_TABLE_NAME where $PLAYLIST_ID_ROW = :id")
    fun findPlayListByID(id: String) :PlayList

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayList(playList: PlayList)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updatePlayList(playList: PlayList)

    @Delete
    fun deletePlayList(playList: PlayList)
}