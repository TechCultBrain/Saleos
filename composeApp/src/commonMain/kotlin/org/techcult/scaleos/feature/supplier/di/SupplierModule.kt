package org.techcult.scaleos.feature.supplier.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.AppDatabase
import org.techcult.scaleos.feature.supplier.data.repository.SupplierRepositoryImpl
import org.techcult.scaleos.feature.supplier.domain.repository.SupplierRepository

val supplierModule= module {
    single { get<AppDatabase>().supplierDao()}
    single {
        SupplierRepositoryImpl(get())
    }.bind<SupplierRepository>()

}