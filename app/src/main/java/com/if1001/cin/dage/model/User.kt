package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "user")
data class User(@ColumnInfo(name = "image_url") var imageURL: String,
                @ColumnInfo(name = "name") var name: String,
                @ColumnInfo(name = "email") var email: String,
                @ColumnInfo(name = "id") @PrimaryKey var id: String){
}