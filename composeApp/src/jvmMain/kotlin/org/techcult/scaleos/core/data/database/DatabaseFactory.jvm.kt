package org.techcult.scaleos.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<AppDatabase> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), AppDatabase.DB_NAME)

        return Room.databaseBuilder<AppDatabase>(name =dbFile.absolutePath )
    }
}