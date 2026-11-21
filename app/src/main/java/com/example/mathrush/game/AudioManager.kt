package com.example.mathrush.game

import android.media.AudioManager as AndroidAudioManager
import android.media.ToneGenerator

class AudioManager {
    private val toneGen = ToneGenerator(AndroidAudioManager.STREAM_MUSIC, 80)

    fun playCorrectSound() {
        try {
            toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
        } catch (_: Exception) {}
    }

    fun playIncorrectSound() {
        try {
            toneGen.startTone(ToneGenerator.TONE_PROP_NACK, 200)
        } catch (_: Exception) {}
    }

    fun playComboSound() {
        try {
            toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 150)
        } catch (_: Exception) {}
    }

    fun playGameOverSound() {
        try {
            toneGen.startTone(ToneGenerator.TONE_SUP_ERROR, 300)
        } catch (_: Exception) {}
    }

    fun release() {
        try {
            toneGen.release()
        } catch (_: Exception) {}
    }
}
