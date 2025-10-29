package org.techcult.scaleos.core.data.database

import androidx.room.RoomDatabaseConstructor

actual object AppDatabaseConstructor :
    RoomDatabaseConstructor<AppDatabase> {
    actual override fun initialize(): AppDatabase {
        TODO("Not yet implemented")
    }
}