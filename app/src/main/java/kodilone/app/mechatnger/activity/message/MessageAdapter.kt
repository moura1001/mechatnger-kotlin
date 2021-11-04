package kodilone.app.mechatnger.activity.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kodilone.app.mechatnger.R
import kodilone.app.mechatnger.model.ChatMessage

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
        val text = this.messageList[position].text

        if(holder.itemViewType == CHATTO){
            val viewItem = holder as ChatToItem
            viewItem.itemView.findViewById<TextView>(R.id.textViewChatTo).text = text
        } else{
            val viewItem = holder as ChatFromItem
            viewItem.itemView.findViewById<TextView>(R.id.textViewChatFrom).text = text
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