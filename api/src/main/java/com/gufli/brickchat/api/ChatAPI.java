package com.gufli.brickchat.api;

import com.gufli.brickchat.api.channel.ChatChannel;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.util.Collection;

public class ChatAPI {

    private ChatAPI() {}

    private static ChatManager chatManager;

    public static void registerChatManager(ChatManager manager) {
        chatManager = manager;
    }

    //

    public static void registerChatChannel(ChatChannel channel) {
        chatManager.registerChatChannel(channel);
    }

    public static void unregisterChatChannel(ChatChannel channel) {
        chatManager.unregisterChatChannel(channel);
    }

    public static ChatChannel channelByName(String name) {
        return chatManager.channelByName(name);
    }

    public static Collection<ChatChannel> channels() {
        return chatManager.channels();
    }

    public static void execute(Player player, String message) {
        chatManager.execute(player, message);
    }

    public static void send(ChatChannel channel, Component text) {
        chatManager.send(channel, text);
    }

    public static void send(ChatChannel channel, Player player, String message) {
        chatManager.send(channel, player, message);
    }

}
