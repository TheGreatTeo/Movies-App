package com.example.moviesapp.RoomDB

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListConvertor {
    val gson = Gson()

    @TypeConverter
    fun arrayListToJson(list: List<Int>?): String? {
        return if(list == null) null else gson.toJson(list)
    }

    @TypeConverter
    fun jsonToArrayList(jsonData: String?): List<Int>? {
        return if (jsonData == null) null else gson.fromJson(jsonData, object : TypeToken<List<Int>?>() {}.type)
    }

}