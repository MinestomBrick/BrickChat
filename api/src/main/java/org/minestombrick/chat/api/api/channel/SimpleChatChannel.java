package org.minestombrick.chat.api.api.channel;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

public class SimpleChatChannel implements ChatChannel {

    private final String name;
    private final String activator;
    private final Component format;

    private RestrictedAction action;
    private String permission;

    public SimpleChatChannel(String name, String activator, Component format) {
        this.name = name;
        this.activator = activator;
        this.format = format;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final Component format() {
        return format;
    }

    @Override
    public final String activator() {
        return activator;
    }

    public final void protect(RestrictedAction action, String permission) {
        this.action = action;
        this.permission = permission;
    }

    @Override
    public boolean canRead(Player player) {
        if ( action == RestrictedAction.READ_AND_TALK ) {
            return player.hasPermission(permission) || player.getPermissionLevel() == 4;
        }
        return true;
    }

    @Override
    public boolean canTalk(Player player) {
        if ( action == RestrictedAction.TALK ) {
            return player.hasPermission(permission) || player.getPermissionLevel() == 4;
        }
        return canRead(player);
    }

    public enum RestrictedAction {
        TALK, READ_AND_TALK;
    }
}
