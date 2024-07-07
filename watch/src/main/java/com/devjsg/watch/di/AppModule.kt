package com.devjsg.watch.di

import android.content.Context
import com.devjsg.watch.data.HeartRateRepository
import com.devjsg.watch.domain.GetHeartRateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHeartRateRepository(
        @ApplicationContext context: Context
    ): HeartRateRepository {
        return HeartRateRepository(context)
    }

    @Provides
    @Singleton
    fun provideGetHeartRateUseCase(
        repository: HeartRateRepository
    ): GetHeartRateUseCase {
        return GetHeartRateUseCase(repository)
    }
}