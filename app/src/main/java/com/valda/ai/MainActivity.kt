package com.valda.ai

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.valda.ai.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        adapter = ChatAdapter(messages)
        b.rvChat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        b.rvChat.adapter = adapter

        addMessage("Halo! Aku Valda AI. Ada yang bisa aku bantu?", false)

        b.btnSend.setOnClickListener { sendMessage() }

        b.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else false
        }

        b.btnClear.setOnClickListener {
            messages.clear()
            adapter.submit(messages)
            addMessage("Chat direset. Ada yang bisa aku bantu?", false)
        }
    }

    private fun sendMessage() {
        val text = b.etMessage.text.toString().trim()
        if (text.isEmpty()) return
        b.etMessage.setText("")

        addMessage(text, true)

        val history = messages.toList()
        addMessage("...", false)

        Thread {
            val reply = GeminiApi.sendMessage(history)
            runOnUiThread {
                messages[messages.size - 1] = ChatMessage(reply, false)
                adapter.submit(messages)
                b.rvChat.scrollToPosition(messages.size - 1)
            }
        }.start()
    }

    private fun addMessage(text: String, isUser: Boolean) {
        messages.add(ChatMessage(text, isUser))
        adapter.submit(messages)
        b.rvChat.scrollToPosition(messages.size - 1)
    }
}
