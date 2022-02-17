package org.minestombrick.chat.app;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.minestombrick.chat.api.api.ChatAPI;
import org.minestombrick.chat.api.api.channel.SimpleChatChannel;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BrickChat extends Extension {

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        BrickChatManager chatManager = new BrickChatManager();
        ChatAPI.registerChatManager(chatManager);

        MinecraftServer.getGlobalEventHandler()
                .addListener(new PlayerChatListener(chatManager));

        // load channels
        loadChannels();

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

    private void loadChannels() {
        try (
                InputStream is = getResource("config.json");
                InputStreamReader isr = new InputStreamReader(is);
        ) {
            JsonObject config = JsonParser.parseReader(isr).getAsJsonObject();
            JsonObject channels = config.get("channels").getAsJsonObject();
            for ( String name : channels.keySet() ) {
                JsonObject channel = channels.get(name).getAsJsonObject();
                String format = channel.get("format").getAsString();
                if ( format == null ) {
                    continue;
                }

                String activator = "";
                if ( channel.has("activator") ) {
                    activator = channel.get("activator").getAsString();
                }

                SimpleChatChannel scc = new SimpleChatChannel(name, activator, Component.text(format));
                ChatAPI.get().registerChatChannel(scc);

                if ( channel.has("protect") ) {
                    JsonObject protect = channel.get("protect").getAsJsonObject();
                    SimpleChatChannel.RestrictedAction action = SimpleChatChannel.RestrictedAction.READ_AND_TALK;
                    if (protect.has("type")) {
                        action = SimpleChatChannel.RestrictedAction.valueOf(protect.get("type").getAsString()
                                .toUpperCase().replace("-", "_"));
                    }

                    if ( !protect.has("permission") ) {
                        continue;
                    }

                    scc.protect(action, protect.get("permission").getAsString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
