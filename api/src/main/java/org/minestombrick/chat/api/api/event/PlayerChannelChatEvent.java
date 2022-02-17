package org.minestombrick.chat.api.api.event;

import org.minestombrick.chat.api.api.channel.ChatChannel;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.Set;

public class PlayerChannelChatEvent implements Event, CancellableEvent {

    private final Player player;

    private final ChatChannel chatChannel;
    private final String message;
    private final Set<Player> recipients;

    private Component format;

    private boolean cancelled = false;

    public PlayerChannelChatEvent(Player player, ChatChannel chatChannel, String message, Set<Player> recipients, Component format) {
        this.player = player;
        this.chatChannel = chatChannel;
        this.message = message;
        this.recipients = recipients;
        this.format = format;
    }

    public ChatChannel chatChannel() {
        return chatChannel;
    }

    public String message() {
        return message;
    }

    public Component format() {
        return format;
    }

    public Set<Player> recipients() {
        return recipients;
    }

    public void changeFormat(Component format) {
        this.format = format;
    }

    public Player player() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
