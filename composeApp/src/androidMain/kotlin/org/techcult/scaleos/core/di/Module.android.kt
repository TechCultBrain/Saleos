package org.techcult.scaleos.core.di

import android.content.Context
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.DatabaseFactory
import org.techcult.scaleos.core.data.preferences.createDataStore

actual val platformModule: Module=
    module{
        single<HttpClientEngine> { OkHttp.create() }

        single {
            createDataStore(context = get<Context>())
        }
        single {
            DatabaseFactory(context = get())
        }
}
