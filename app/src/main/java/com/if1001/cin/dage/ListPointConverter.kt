package com.if1001.cin.dage

import android.arch.persistence.room.TypeConverter
import android.graphics.PointF
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListPointConverter {
    val gson = Gson()

    @TypeConverter
    fun routeListToGson(route: List<PointF>) :String{
        return this.gson.toJson(route)
    }

    @TypeConverter
    fun routeGsonToList(route: String): List<PointF>{
        return gson.fromJson(route, object : TypeToken<List<PointF>>() {}.type)
    }
}