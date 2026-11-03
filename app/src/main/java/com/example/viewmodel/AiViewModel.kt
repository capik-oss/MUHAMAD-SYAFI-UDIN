package com.example.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

private const val TAG = "AnsorAiViewModel"

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isStreaming: Boolean = false
)

enum class MessageSender {
    USER, ASSISTANT
}

sealed interface AiUiState {
    data object Idle : AiUiState
    data object Loading : AiUiState
    data class Streaming(val partialText: String) : AiUiState
    data class Success(val fullText: String) : AiUiState
    data class Error(val message: String) : AiUiState
}

class AiViewModel : ViewModel() {

    // Mandated: Initialize Firebase AI Logic model lazily
    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash"
        )
    }

    private val _uiState = MutableStateFlow<AiUiState>(AiUiState.Idle)
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.ASSISTANT,
                text = "Assalamu'alaikum Sahabat! Saya Asisten Cerdas Sahabat Ansor Diwek. Siap membantu penjelasan PD/PRT, ke-Aswaja-an, amaliyah NU, jenjang kaderisasi (PKD/Diklatsar), maupun ide pengembangan Lapak Kader di wilayah Diwek. Ada yang bisa saya bantu hari ini?"
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val systemContext = """
        Anda adalah Asisten Virtual Resmi Sahabat Ansor Diwek (Pimpinan Anak Cabang GP Ansor Kecamatan Diwek, Kabupaten Jombang, Jawa Timur).
        Karakter Anda:
        - Santun, berwibawa, bersemangat membina generasi muda (ghirah kepemudaan).
        - Selalu menyapa dengan 'Sahabat' untuk kader Ansor, atau 'Ndan' jika membahas Barisan Ansor Serbaguna (Banser).
        - Menguasai Peraturan Dasar (PD) & Peraturan Rumah Tangga (PRT) GP Ansor, sejarah pendirian Ansor (Banyuwangi 1934), peran para muassis NU (KH. Hasyim Asy'ari Tebuireng, KH. Wahab Chasbullah Tambakberas, KH. Bisri Syansuri Denanyar).
        - Berlandaskan akidah Islam Ahlussunnah wal Jama'ah an-Nahdliyyah (Tawasuth, Tawazun, I'tidal, Tasamuh).
        - Mampu memberikan panduan fiqih ibadah sehari-hari mazhab Syafi'i.
        - Memberi ide praktis dan motivasi kemandirian ekonomi kader (UMKM/Lapak Kader di desa-desa Diwek seperti Cukir, Kwaron, Bulurejo, Jatipelem, Kayangan, dll).
        Tanggapi pertanyaan berikut dengan jelas, hangat, terstruktur, dan inspiratif:
    """.trimIndent()

    fun sendMessage(userPrompt: String) {
        val trimmed = userPrompt.trim()
        if (trimmed.isEmpty()) return

        val userMessage = ChatMessage(sender = MessageSender.USER, text = trimmed)
        val assistantPlaceholderId = java.util.UUID.randomUUID().toString()

        _messages.value = _messages.value + userMessage + ChatMessage(
            id = assistantPlaceholderId,
            sender = MessageSender.ASSISTANT,
            text = "...",
            isStreaming = true
        )

        viewModelScope.launch {
            _uiState.value = AiUiState.Loading
            val accumulated = StringBuilder()

            try {
                // Build prompt with rich context
                val combinedPrompt = "$systemContext\n\nPertanyaan Kader:\n$trimmed"

                // Mandated: realistic 30-60s timeout window
                withTimeout(60_000L) {
                    model.generateContentStream(combinedPrompt).collect { chunk ->
                        val chunkText = chunk.text.orEmpty()
                        accumulated.append(chunkText)
                        val currentText = accumulated.toString()

                        _uiState.value = AiUiState.Streaming(currentText)

                        // Update assistant streaming message in list
                        _messages.value = _messages.value.map { msg ->
                            if (msg.id == assistantPlaceholderId) {
                                msg.copy(text = currentText, isStreaming = true)
                            } else {
                                msg
                            }
                        }
                    }
                }

                val finalResponse = accumulated.toString().trim()
                if (finalResponse.isNotEmpty()) {
                    _uiState.value = AiUiState.Success(finalResponse)
                    _messages.value = _messages.value.map { msg ->
                        if (msg.id == assistantPlaceholderId) {
                            msg.copy(text = finalResponse, isStreaming = false)
                        } else {
                            msg
                        }
                    }
                } else {
                    val fallback = "Mohon maaf Sahabat, sistem tidak menerima jawaban lengkap. Silakan coba tanyakan kembali."
                    _uiState.value = AiUiState.Error(fallback)
                    _messages.value = _messages.value.map { msg ->
                        if (msg.id == assistantPlaceholderId) {
                            msg.copy(text = fallback, isStreaming = false)
                        } else {
                            msg
                        }
                    }
                }
            } catch (e: TimeoutCancellationException) {
                Log.e(TAG, "AI streaming timed out", e)
                val timeoutMsg = "Koneksi ke Gemini AI melebihi batas waktu (60 detik). Silakan periksa jaringan dan coba ulangi."
                _uiState.value = AiUiState.Error(timeoutMsg)
                _messages.value = _messages.value.map { msg ->
                    if (msg.id == assistantPlaceholderId) {
                        msg.copy(text = timeoutMsg, isStreaming = false)
                    } else {
                        msg
                    }
                }
            } catch (e: CancellationException) {
                // Mandated: ALWAYS re-throw CancellationException
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Gemini AI generation failed", e)
                val errorMsg = "Terjadi kendala saat menghubungkan ke Gemini AI: ${e.localizedMessage ?: "Gagal memproses"}"
                _uiState.value = AiUiState.Error(errorMsg)
                _messages.value = _messages.value.map { msg ->
                    if (msg.id == assistantPlaceholderId) {
                        msg.copy(text = errorMsg, isStreaming = false)
                    } else {
                        msg
                    }
                }
            }
        }
    }

    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                sender = MessageSender.ASSISTANT,
                text = "Percakapan telah direset. Silakan ajukan pertanyaan seputar PD/PRT, keorganisasian Ansor Diwek, atau Lapak Kader!"
            )
        )
        _uiState.value = AiUiState.Idle
    }
}
