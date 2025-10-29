package org.techcult.scaleos.core.data.database

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class Converters {


    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? =
        value?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(value) }

    @TypeConverter
    fun fromTagList(tags: List<String>?): String? =
        tags?.joinToString(",")

    @TypeConverter
    fun toTagList(tags: String?): List<String>? =
        tags?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
}