package org.techcult.scaleos.core.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initScaleOsKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            coreModule,platformModule

            )
    }
}