package com.example.local_network_scanner.di

import android.content.Context
import com.example.local_network_scanner.ui.feedback.HapticFeedbackManager
import com.example.local_network_scanner.ui.feedback.SoundFeedbackManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedbackModule {

    @Provides
    @Singleton
    fun provideSoundFeedbackManager(@ApplicationContext context: Context): SoundFeedbackManager {
        return SoundFeedbackManager(context)
    }

    @Provides
    @Singleton
    fun provideHapticFeedbackManager(@ApplicationContext context: Context): HapticFeedbackManager {
        return HapticFeedbackManager(context)
    }
}
