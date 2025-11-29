package org.techcult.scaleos.feature.product.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.AppDatabase
import org.techcult.scaleos.feature.product.data.repository.BrandRepositoryImpl
import org.techcult.scaleos.feature.product.data.repository.CategoryRepositoryImpl
import org.techcult.scaleos.feature.product.data.repository.DepartmentRepositoryImpl
import org.techcult.scaleos.feature.product.data.repository.UnitRepositoryImpl
import org.techcult.scaleos.feature.product.domain.repository.BrandRepository
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository
import org.techcult.scaleos.feature.product.domain.repository.DepartmentRepository
import org.techcult.scaleos.feature.product.domain.repository.UnitRepository

val productModule = module {
    single {
        get<AppDatabase>().categoryDao()
    }
    single {
        get<AppDatabase>().unitDao()
    }
    single {
        get<AppDatabase>().brandDao()
    }
    single {
        get<AppDatabase>().departmentDao()

    }
    single {
        CategoryRepositoryImpl(get())
    }.bind<CategoryRepository>()
    single {
        UnitRepositoryImpl(get())
    }.bind<UnitRepository>()

    single {
        BrandRepositoryImpl(get())
    }.bind<BrandRepository>()

    single {
        DepartmentRepositoryImpl(get())
    }.bind<DepartmentRepository>()
}