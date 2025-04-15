package com.jeklov.post.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeklov.post.data.models.Tag
import java.net.URI

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTagList(value: List<Tag>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTagList(value: String?): List<Tag> {
        val listType = object : TypeToken<List<Tag>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun fromUriList(value: List<URI>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toUriList(value: String?): List<URI> {
        val listType = object : TypeToken<List<URI>>() {}.type
        return gson.fromJson(value, listType) ?: emptyList()
    }
}