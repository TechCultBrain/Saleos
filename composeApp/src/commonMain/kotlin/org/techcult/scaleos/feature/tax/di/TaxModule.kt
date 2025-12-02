package org.techcult.scaleos.feature.tax.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.AppDatabase
import org.techcult.scaleos.feature.supplier.data.repository.SupplierRepositoryImpl
import org.techcult.scaleos.feature.supplier.domain.repository.SupplierRepository
import org.techcult.scaleos.feature.tax.data.repository.TaxRepositoryImpl
import org.techcult.scaleos.feature.tax.domain.repository.TaxRepository

val taxModule= module {

    single { get<AppDatabase>().taxSlabDao()}
    single {
        TaxRepositoryImpl(get())
    }.bind<TaxRepository>()
}