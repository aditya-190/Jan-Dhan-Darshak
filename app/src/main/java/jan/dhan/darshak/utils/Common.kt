package jan.dhan.darshak.utils

import android.speech.tts.TextToSpeech

object Common {
    fun sayOutLoud(textToSpeech: TextToSpeech, message: String) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}