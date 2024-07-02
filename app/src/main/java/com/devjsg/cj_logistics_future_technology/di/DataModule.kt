package com.devjsg.cj_logistics_future_technology.di

import android.content.Context
import com.devjsg.cj_logistics_future_technology.data.datasync.DataSyncManager
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable
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
    fun provideDataClient(@ApplicationContext context: Context): DataClient {
        return Wearable.getDataClient(context)
    }

    @Provides
    @Singleton
    fun provideDataSyncManager(dataClient: DataClient): DataSyncManager {
        return DataSyncManager(dataClient)
    }
}