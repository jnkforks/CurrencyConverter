package com.friszing.rates.di.module

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
open class StorageModule {

    @Singleton
    @Named("RepositoryConfig")
    @Provides
    open fun provideConfigurationPreferences(
        application: Application
    ): SharedPreferences =
        application.getSharedPreferences(
            CONFIGURATION_PREF,
            MODE_PRIVATE
        )

    private companion object {
        private const val CONFIGURATION_PREF = "CONFIGURATION_PREF"
    }
}