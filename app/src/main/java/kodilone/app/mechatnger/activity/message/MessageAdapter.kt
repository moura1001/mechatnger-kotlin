package kodilone.app.mechatnger.activity.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kodilone.app.mechatnger.R
import kodilone.app.mechatnger.model.User

class MessageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messageList: List<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            0 -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_to_row, parent, false)

                return ChatToItem(itemView)
            }
            1 -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_from_row, parent, false)

                return ChatFromItem(itemView)
            }
            else -> {
                val itemView : View? = null
                return object : RecyclerView.ViewHolder(itemView!!) {}
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            0 -> {
                val viewItem = holder as ChatToItem
                return
            }
            1 -> {
                val viewItem = holder as ChatFromItem
                return
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList!!.size
    }

    fun setMessages(messageList: List<String>?) {
        this.messageList = messageList
    }

    class ChatToItem internal constructor(view: View) : RecyclerView.ViewHolder(view) {}

    class ChatFromItem internal constructor(view: View) : RecyclerView.ViewHolder(view) {}
}