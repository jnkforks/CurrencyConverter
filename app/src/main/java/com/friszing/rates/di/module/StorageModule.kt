package com.friszing.rates.di.module

import android.app.Application
import android.content.Context.MODE_PRIVATE
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Named("RepositoryConfig")
    @Provides
    fun provideConfigurationPreferences(application: Application) =
        application.getSharedPreferences(CONFIGURATION_PREF, MODE_PRIVATE)

    private companion object {
        private const val CONFIGURATION_PREF = "CONFIGURATION_PREF"
    }
}