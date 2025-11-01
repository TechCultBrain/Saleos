package org.techcult.scaleos.feature.settings.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.AppDatabase
import org.techcult.scaleos.feature.product.data.repository.CategoryRepositoryImpl
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsViewModel

val settingsModule= module {
    single {
        get<AppDatabase>().categoryDao()
    }


    single {
        CategoryRepositoryImpl(get())
    }.bind<CategoryRepository>()

    viewModel {
        CategorySettingsViewModel(get())

    }
}