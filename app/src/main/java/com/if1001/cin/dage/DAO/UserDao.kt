package com.if1001.cin.dage.DAO

import android.arch.persistence.room.*
import com.if1001.cin.dage.model.USER_TABLE_NAME
import com.if1001.cin.dage.model.User

/**
 * Classe de acesso a dados (BD) de usuário
 */
@Dao
interface UserDao {
    @Query("select * from $USER_TABLE_NAME limit 1")
    fun findUSer(): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}