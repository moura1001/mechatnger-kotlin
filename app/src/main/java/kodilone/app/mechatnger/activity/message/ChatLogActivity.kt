package kodilone.app.mechatnger.activity.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import kodilone.app.mechatnger.R

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        val chatLog = findViewById<RecyclerView>(R.id.recyclerViewChatLog)
        val messageAdapter = MessageAdapter()
        chatLog.setAdapter(messageAdapter)

        val messageList = listOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
        messageAdapter.setMessages(messageList)
    }
}