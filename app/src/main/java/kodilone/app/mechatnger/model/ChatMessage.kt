package kodilone.app.mechatnger.model

class ChatMessage(var id: String, var text: String, var fromId: String, var timestamp: Long) {
    constructor() : this("", "", "", -1)
}