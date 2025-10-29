package org.techcult.scaleos.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return createDataStore(
        producePath = {
            (context as Context)
                .filesDir
                .resolve(
                    datastoreFileName
                ).absolutePath
        }
    )
}