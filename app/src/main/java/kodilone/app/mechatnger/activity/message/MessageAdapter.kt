package kodilone.app.mechatnger.activity.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kodilone.app.mechatnger.R
import kodilone.app.mechatnger.model.ChatMessage
import kodilone.app.mechatnger.model.User

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val CHATTO = 0
    private val CHATFROM = 1

    private val messageList: MutableList<ChatMessage> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == CHATTO){
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_to_row, parent, false)

            return ChatToItem(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_from_row, parent, false)

            return ChatFromItem(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = this.messageList[position]
        val text = message.text

        if(holder.itemViewType == CHATTO){
            val user = LatestMessagesActivity.currentUser!!
            val viewItem = holder as ChatToItem
            viewItem.itemView.findViewById<TextView>(R.id.textViewChatTo).text = text
            val targetImageView = viewItem.itemView.findViewById<ImageView>(R.id.imageViewChatTo)
            Picasso.get().load(user.profileImageUrl).into(targetImageView)

        } else{
            val viewItem = holder as ChatFromItem
            viewItem.itemView.findViewById<TextView>(R.id.textViewChatFrom).text = text
            var user: User?
            val uid = message.fromId
            val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
            ref.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)
                    Log.d("MessageAdapter", "From user: ${user?.userName}")
                    val targetImageView = viewItem.itemView.findViewById<ImageView>(R.id.imageViewChatFrom)
                    Picasso.get().load(user?.profileImageUrl).into(targetImageView)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    override fun getItemCount(): Int {
        return messageList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = this.messageList[position]
        if(message.type.equals(ChatMessage.MessageType.LEFT)){
            return CHATFROM
        }else {
            return CHATTO
        }
    }

    fun addMessage(chatMessage: ChatMessage) {
        this.messageList.add(chatMessage)
        notifyDataSetChanged()
    }

    class ChatToItem internal constructor(view: View) : RecyclerView.ViewHolder(view) {}

    class ChatFromItem internal constructor(view: View) : RecyclerView.ViewHolder(view) {}
}