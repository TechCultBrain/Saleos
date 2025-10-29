package org.techcult.scaleos.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import java.io.File

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return createDataStore(
        producePath = {
            val file = File(System.getProperty("java.io.tmpdir"), datastoreFileName)
            file.absolutePath
        }
    )
}