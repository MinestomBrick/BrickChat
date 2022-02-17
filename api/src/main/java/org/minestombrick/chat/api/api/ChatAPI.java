package org.minestombrick.chat.api.api;

public class ChatAPI {

    private ChatAPI() {}

    private static ChatManager chatManager;

    public static void registerChatManager(ChatManager manager) {
        chatManager = manager;
    }

    //

    public static ChatManager get() {
        return chatManager;
    }

}
