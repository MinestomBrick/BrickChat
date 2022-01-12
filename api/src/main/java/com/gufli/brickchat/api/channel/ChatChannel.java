package com.gufli.brickchat.api.channel;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

public interface ChatChannel {

    String name();

    Component format();

    String activator();

    boolean canRead(Player player);

    boolean canTalk(Player player);

}
