package com.gufli.brickchat.api;

import com.gufli.brickchat.api.channel.ChatChannel;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.util.Collection;

public interface ChatManager {

    void registerChatChannel(ChatChannel channel);

    void unregisterChatChannel(ChatChannel channel);

    ChatChannel channelByName(String name);

    Collection<ChatChannel> channels();

    void handle(Player player, String message);

    void send(ChatChannel channel, Component text);

    void send(ChatChannel channel, Player player, String message);

}
