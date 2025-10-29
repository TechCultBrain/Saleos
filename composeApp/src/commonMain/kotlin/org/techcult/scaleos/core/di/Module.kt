package org.techcult.scaleos.core.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.DatabaseFactory

expect val platformModule: Module

val coreModule = module {

    includes(platformModule)
    single {
        get<DatabaseFactory>().create()

            .setDriver(BundledSQLiteDriver())
            .build()

    }

}