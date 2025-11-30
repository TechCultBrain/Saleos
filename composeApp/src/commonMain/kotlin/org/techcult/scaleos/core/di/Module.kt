package org.techcult.scaleos.core.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.module.Module
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.DatabaseFactory
import org.techcult.scaleos.feature.product.di.productModule
import org.techcult.scaleos.feature.settings.di.settingsModule
import org.techcult.scaleos.feature.supplier.di.supplierModule

expect val platformModule: Module

val coreModule = module {


    includes(settingsModule,platformModule,productModule, supplierModule)
    single {
        get<DatabaseFactory>().create()

            .setDriver(BundledSQLiteDriver())
            .build()

    }

}