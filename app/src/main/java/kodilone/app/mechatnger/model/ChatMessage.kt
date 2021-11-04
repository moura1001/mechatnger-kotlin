package kodilone.app.mechatnger.model

class ChatMessage(var id: String, var text: String, var fromId: String, var timestamp: Long,
                  var type: MessageType = MessageType.RIGHT) {
    enum class MessageType {
        LEFT, RIGHT
    }

    constructor() : this("", "", "", -1)
}