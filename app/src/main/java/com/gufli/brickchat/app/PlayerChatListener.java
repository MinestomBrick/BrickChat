package com.gufli.brickchat.app;

import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerChatListener implements EventListener<PlayerChatEvent> {

    private final BrickChatManager chatManager;

    public PlayerChatListener(BrickChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @Override
    public @NotNull Class<PlayerChatEvent> eventType() {
        return PlayerChatEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerChatEvent event) {
        chatManager.handle(event);
        return Result.SUCCESS;
    }
}
