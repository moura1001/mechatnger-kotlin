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
import kotlin.random.Random

class ChatLogActivity : AppCompatActivity() {
    companion object{
        val TAG = "ChatLogScreen"
    }

    var recyclerViewChatLog: RecyclerView? = null
    var messageAdapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        recyclerViewChatLog = findViewById(R.id.recyclerViewChatLog)

        recyclerViewChatLog!!.setAdapter(messageAdapter)
        listenForMessages()

        findViewById<Button>(R.id.sendButtonChatLog).setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d(TAG, chatMessage?.text!!)
                if(chatMessage.fromId == LatestMessagesActivity.USER!!.uid){
                    chatMessage.type = ChatMessage.MessageType.RIGHT
                } else{
                    chatMessage.type = ChatMessage.MessageType.LEFT
                }
                /*if(Random.nextInt(0, 100) > 50){
                    chatMessage.type = ChatMessage.MessageType.RIGHT
                } else{
                    chatMessage.type = ChatMessage.MessageType.LEFT
                }*/
                messageAdapter.addMessage(chatMessage)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun performSendMessage(){
        val text = findViewById<EditText>(R.id.editTextChatLog).text.toString()
        val fromId = LatestMessagesActivity.USER?.uid!!

        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatmessage = ChatMessage(ref.key!!, text, fromId, System.currentTimeMillis()/1000)

        ref.setValue(chatmessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${ref.key}")
            }
    }
}