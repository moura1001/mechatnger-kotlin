package kodilone.app.mechatnger.activity.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kodilone.app.mechatnger.R
import kodilone.app.mechatnger.model.ChatMessage

class ChatLogActivity : AppCompatActivity() {
    companion object{
        val TAG = "ChatLogScreen"
    }

    var recyclerViewChatLog: RecyclerView? = null
    var messageAdapter = MessageAdapter()
    var messageList: MutableList<ChatMessage>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        recyclerViewChatLog = findViewById(R.id.recyclerViewChatLog)

        recyclerViewChatLog!!.setAdapter(messageAdapter)
        messageAdapter.setMessages(messageList)

        findViewById<Button>(R.id.sendButtonChatLog).setOnClickListener {
            performSendMessage()
        }
    }

    private fun performSendMessage(){
        val text = findViewById<EditText>(R.id.editTextChatLog).text.toString()
        val fromId = LatestMessagesActivity.USER?.uid!!

        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatmessage = ChatMessage(ref.key!!, text, fromId, System.currentTimeMillis()/1000)

        ref.setValue(chatmessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${ref.key}")
                messageList!!.add(chatmessage)
                messageAdapter.setMessages(messageList)
            }
    }
}