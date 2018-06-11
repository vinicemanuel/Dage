package com.if1001.cin.dage.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val USER_TABLE_NAME = "user"
const val USER_NAME_ROW = "name"
const val USER_ID_ROW = "id"
const val USER_EMAIL_ROW = "email"
const val USER_IMG_URL_ROW = "image_url"

@Entity(tableName = USER_TABLE_NAME)
data class User(@ColumnInfo(name = USER_IMG_URL_ROW) var imageURL: String,
                @ColumnInfo(name = USER_NAME_ROW) var name: String,
                @ColumnInfo(name = USER_EMAIL_ROW) var email: String,
                @ColumnInfo(name = USER_ID_ROW) @PrimaryKey var id: String){
}