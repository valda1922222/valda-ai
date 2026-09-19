package com.valda.ai

import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GeminiApi {

    // API key kamu (AQ.Ab8RN...)
    private const val API_KEY = "AQ.Ab8RN6KIq8pmgloNC3Id_z5UxRJ0KbxUwYUo4Zftar_7F74QJw"

    // ✅ MODEL BARU: gemini-3.6-flash
    private const val ENDPOINT =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent"

    fun sendMessage(history: List<ChatMessage>): String {
        try {
            val url = URL(ENDPOINT)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("x-goog-api-key", API_KEY)
            conn.doOutput = true
            conn.connectTimeout = 30000
            conn.readTimeout = 30000

            val contents = JSONArray()
            for (m in history) {
                val role = if (m.isUser) "user" else "model"
                val part = JSONObject().put("text", m.text)
                val parts = JSONArray().put(part)
                val content = JSONObject()
                    .put("role", role)
                    .put("parts", parts)
                contents.put(content)
            }

            val body = JSONObject().put("contents", contents).toString()

            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(body)
            writer.flush()
            writer.close()

            val code = conn.responseCode
            if (code !in 200..299) {
                val err = conn.errorStream?.bufferedReader()?.use { it.readText() } ?: ""
                return "Error $code: $err"
            }

            val response = conn.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(response)
            val candidates = json.optJSONArray("candidates")
            if (candidates == null || candidates.length() == 0) {
                return "Maaf, tidak ada jawaban."
            }
            val content = candidates.getJSONObject(0).getJSONObject("content")
            val parts = content.getJSONArray("parts")
            return parts.getJSONObject(0).optString("text", "(kosong)")

        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }
}
