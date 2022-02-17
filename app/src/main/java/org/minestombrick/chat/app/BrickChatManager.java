package org.minestombrick.chat.app;

import org.minestombrick.chat.api.api.ChatManager;
import org.minestombrick.chat.api.api.channel.ChatChannel;
import org.minestombrick.chat.api.api.event.PlayerChannelChatEvent;
import com.gufli.brickplaceholders.api.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.tag.Tag;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BrickChatManager implements ChatManager {

    private final Tag<String> DEFAULT_CHANNEL_TAG = Tag.String("DEFAULT_CHAT_CHANNEL");

    private final Set<ChatChannel> chatChannels = new CopyOnWriteArraySet<>();

    @Override
    public void registerChatChannel(ChatChannel channel) {
        if (channelByName(channel.name()) != null) {
            throw new IllegalArgumentException("A channel with that name already exists.");
        }
        chatChannels.add(channel);
    }

    @Override
    public void unregisterChatChannel(ChatChannel channel) {
        chatChannels.remove(channel);
    }

    @Override
    public ChatChannel channelByName(String name) {
        return chatChannels.stream().filter(c -> c.name().equals(name))
                .findFirst().orElse(null);
    }

    @Override
    public Collection<ChatChannel> channels() {
        return Collections.unmodifiableCollection(chatChannels);
    }

    void execute(PlayerChatEvent event) {
        Player player = event.getPlayer();

        PlayerChannelChatEvent channelChatEvent = dispatch(player, event.getMessage());
        if (channelChatEvent == null) {
            event.setCancelled(true);
            return;
        }

        // update minestom event
        event.setMessage(channelChatEvent.message());
        event.getRecipients().clear();
        event.getRecipients().addAll(channelChatEvent.recipients());
        event.setChatFormat(evt -> format(channelChatEvent.format(), player, event.getMessage()));
    }

    @Override
    public void execute(Player player, String message) {
        PlayerChannelChatEvent event = dispatch(player, message);
        if (event == null) return;
        send(event.recipients(), event.player(), event.format(), event.message());
    }

    @Override
    public void send(ChatChannel channel, Player player, String message) {
        send(MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                        .filter(channel::canRead).collect(Collectors.toSet()),
                player, channel.format(), message);
    }

    @Override
    public void send(ChatChannel channel, Component text) {
        MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(channel::canRead)
                .forEach(p -> p.sendMessage(text));
    }

    private void send(Collection<Player> recipients, Player player, Component format, String message) {
        Component result = format(format, player, message);
        recipients.forEach(p -> p.sendMessage(result));
    }

    private Component format(Component format, Player player, String message) {
        Component result = format
                .replaceText(builder -> {
                    builder.match(Pattern.quote("{playername}"))
                            .replacement(player.getName());
                })
                .replaceText(builder -> {
                    builder.match(Pattern.quote("{chatmessage}"))
                            .replacement(Component.text(message));
                });

        if ( MinecraftServer.getExtensionManager().hasExtension("brickplaceholders") ) {
            result = PlaceholderAPI.replace(player, result);
        }

        return result;
    }

    private PlayerChannelChatEvent dispatch(Player player, String message) {
        message = message.trim();

        List<ChatChannel> channels = chatChannels.stream()
                .filter(c -> c.canTalk(player))
                .sorted(Comparator.comparingInt(ch -> ch.activator() == null ? 0 : -ch.activator().length()))
                .toList();

        // initialize default channel
        ChatChannel channel = null;
        if (player.hasTag(DEFAULT_CHANNEL_TAG)) {
            String name = player.getTag(DEFAULT_CHANNEL_TAG);
            channel = channelByName(name);
        }

        // unset default chat channel by just typing the prefix
        if (channel != null && channel.activator() != null && channel.activator().equals(message)) {
            player.removeTag(DEFAULT_CHANNEL_TAG);
            return null;
        }

        // set default chat channel by just typing the prefix
        for (ChatChannel ch : channels) {
            if (ch.activator() != null && !ch.activator().equals("") && ch.activator().equals(message)) {
                player.setTag(DEFAULT_CHANNEL_TAG, ch.name());
                return null;
            }
        }

        // find channel for typed prefix
        for (ChatChannel ch : channels) {
            if (ch == channel) {
                continue;
            }

            if (ch.activator() != null && !message.startsWith(ch.activator())) {
                continue;
            }

            // If the player is using a default channel, use that prefix instead to talk in the channel without prefix
            if (channel != null && channel.activator() != null && ch.activator() != null && ch.activator().equals("")) {
                if (message.startsWith(channel.activator())) {
                    message = message.replaceFirst(Pattern.quote(channel.activator()), "");
                    channel = ch;
                    break;
                }
                continue;
            }

            if (ch.activator() != null) {
                message = message.substring(ch.activator().length());
            }

            channel = ch;
            break;
        }

        if (channel == null) {
            return null;
        }

        PlayerChannelChatEvent channelChatEvent = dispatch(channel, player, message);
        if (channelChatEvent.isCancelled() || channelChatEvent.recipients().isEmpty()) {
            return null;
        }

        return channelChatEvent;
    }

    private PlayerChannelChatEvent dispatch(ChatChannel channel, Player player, String message) {
        Set<Player> receivers = MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                .filter(channel::canRead).collect(Collectors.toSet());

        PlayerChannelChatEvent event = new PlayerChannelChatEvent(player, channel, message, receivers, channel.format());
        MinecraftServer.getGlobalEventHandler().call(event);

        return event;
    }
}
