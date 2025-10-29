package org.techcult.scaleos.core.data.database

actual object AppDatabaseConstructor :
    androidx.room.RoomDatabaseConstructor<org.techcult.scaleos.core.data.database.AppDatabase> {
    actual override fun initialize(): org.techcult.scaleos.core.data.database.AppDatabase {
        TODO("Not yet implemented")
    }
}