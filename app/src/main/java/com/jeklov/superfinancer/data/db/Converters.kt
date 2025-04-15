package com.jeklov.superfinancer.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jeklov.superfinancer.data.models.news.Multimedia
import java.util.Date

internal class Converters {

    private val gson: Gson = GsonBuilder().create()

    // Converter for Date
    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Converter for List<String>
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        return value?.split(",") ?: emptyList()
    }

    // Converter for List<Multimedia>
    @TypeConverter
    fun fromMultimediaList(value: List<Multimedia>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toMultimediaList(value: String?): List<Multimedia>? {
        return value?.let { gson.fromJson(it, Array<Multimedia>::class.java).toList() }
    }

    // Converter for List<Any>
    @TypeConverter
    fun fromAnyList(value: List<Any>?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAnyList(value: String?): List<Any>? {
        return value?.let { gson.fromJson(it, Array<Any>::class.java).toList() }
    }
}