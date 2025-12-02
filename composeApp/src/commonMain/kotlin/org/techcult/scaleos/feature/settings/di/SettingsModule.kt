package org.techcult.scaleos.feature.settings.di

import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.techcult.scaleos.core.data.database.AppDatabase
import org.techcult.scaleos.feature.product.data.repository.CategoryRepositoryImpl
import org.techcult.scaleos.feature.product.data.repository.UnitRepositoryImpl
import org.techcult.scaleos.feature.product.domain.repository.CategoryRepository
import org.techcult.scaleos.feature.product.domain.repository.UnitRepository
import org.techcult.scaleos.feature.settings.presentation.viewmodel.BrandSettingsViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.CategorySettingsViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.DepartmentSettingsViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.SupplierSettingViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.TaxSettingsViewModel
import org.techcult.scaleos.feature.settings.presentation.viewmodel.UnitSettingViewModel

val settingsModule= module {





    viewModel {
        CategorySettingsViewModel(get())

    }



    viewModel {
        UnitSettingViewModel(get())

    }

    viewModel {
        BrandSettingsViewModel(get())

    }
    viewModel {
        DepartmentSettingsViewModel(get())

    }

    viewModel {
        SupplierSettingViewModel(get())

    }
    viewModel {
        TaxSettingsViewModel(get())

    }
}