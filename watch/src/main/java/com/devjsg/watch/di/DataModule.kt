package com.devjsg.watch.di

import android.content.Context
import com.devjsg.watch.data.DataSyncManager
import com.devjsg.watch.data.HeartRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideHeartRateRepository(
        @ApplicationContext context: Context
    ): HeartRateRepository {
        return HeartRateRepository(context)
    }

    @Provides
    @Singleton
    fun provideDataSyncManager(
        @ApplicationContext context: Context
    ): DataSyncManager {
        return DataSyncManager(context)
    }
}