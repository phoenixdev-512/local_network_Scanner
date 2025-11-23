package com.example.local_network_scanner.ui.feedback

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.local_network_scanner.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundFeedbackManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()
    
    // Sound IDs
    private var tapSoundId: Int = 0
    private var successSoundId: Int = 0
    private var errorSoundId: Int = 0
    private var toggleSoundId: Int = 0
    private var swipeSoundId: Int = 0
    private var notificationSoundId: Int = 0
    
    init {
        loadSounds()
    }
    
    private fun loadSounds() {
        // Load Google-style sound assets
        // Note: These resources need to be added to res/raw/
        // Using try-catch to prevent crashes if resources are missing
        try {
            // tapSoundId = soundPool.load(context, R.raw.sound_tap, 1)
            // successSoundId = soundPool.load(context, R.raw.sound_success, 1)
            // errorSoundId = soundPool.load(context, R.raw.sound_error, 1)
            // toggleSoundId = soundPool.load(context, R.raw.sound_toggle, 1)
            // swipeSoundId = soundPool.load(context, R.raw.sound_swipe, 1)
            // notificationSoundId = soundPool.load(context, R.raw.sound_notification, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun playTap() {
        if (tapSoundId != 0) soundPool.play(tapSoundId, 0.3f, 0.3f, 1, 0, 1.0f)
    }
    
    fun playSuccess() {
        if (successSoundId != 0) soundPool.play(successSoundId, 0.5f, 0.5f, 1, 0, 1.0f)
    }
    
    fun playError() {
        if (errorSoundId != 0) soundPool.play(errorSoundId, 0.5f, 0.5f, 1, 0, 1.0f)
    }
    
    fun playToggle(isOn: Boolean) {
        if (toggleSoundId != 0) {
            val pitch = if (isOn) 1.2f else 0.8f
            soundPool.play(toggleSoundId, 0.4f, 0.4f, 1, 0, pitch)
        }
    }
    
    fun playSwipe() {
        if (swipeSoundId != 0) soundPool.play(swipeSoundId, 0.2f, 0.2f, 1, 0, 1.0f)
    }
    
    fun playNotification() {
        if (notificationSoundId != 0) soundPool.play(notificationSoundId, 0.6f, 0.6f, 1, 0, 1.0f)
    }
    
    fun release() {
        soundPool.release()
    }
}
