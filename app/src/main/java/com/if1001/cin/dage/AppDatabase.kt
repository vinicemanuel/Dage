package com.if1001.cin.dage

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.if1001.cin.dage.DAO.PlayListDao
import com.if1001.cin.dage.DAO.UserDao
import com.if1001.cin.dage.DAO.WorkoutDao
import com.if1001.cin.dage.model.PlayList
import com.if1001.cin.dage.model.User
import com.if1001.cin.dage.model.Workout

/**
 * Define a Base de dados do APP e suas classes mapeadas
 */
@Database(entities = [(User::class), (PlayList::class), (Workout::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun UserDao(): UserDao
    abstract fun PlayListDao(): PlayListDao
    abstract fun WorkoutDao(): WorkoutDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return if (instance != null) {
                instance!!
            } else {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "db-name").allowMainThreadQueries().build()
                return instance!!
            }
        }
    }
}