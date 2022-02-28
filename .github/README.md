# BrickChat

An extension for [Minestom](https://github.com/Minestom/Minestom) to manage chat channels.

## Install

Get the [release](https://github.com/MinestomBrick/BrickChat/releases)
and place it in the extension folder of your minestom server.

## Config

You can change the settings in the `config.json`.

You can remove and create chat channels.
```json
{
  "channels": {
    "default": {
      "format": "{playername} > {chatmessage}"
    },
    "admin": {
      "format": "[ADMINCHAT] {playername} > {chatmessage}",
      "activator": "!",
      "protect": {
        "type": "READ",
        "permission": "brickchat.channel.admin"
      }
    }
  }
}
```

The **activator** is the symbol you start your message with to talk in this channel.

Available **protect** types are: 
* TALK: You can read, but need permission to talk
* READ_AND_TALK: You need permission to read and talk

## API

```
repositories {
    maven { url "https://repo.jorisg.com/snapshots" }
}

dependencies {
    implementation 'com.gufli.brickchat:api:1.0-SNAPSHOT'
}
```

```java
ChatAPI.get().registerChatChannel(new SimpleChatChannel("trade", "$", "[TRADE] {playername} > {chatmessage}"));

ChatChannel channel = ChatAPI.get().channelByName("trade");
ChatAPI.get().send(channel, "hello fellow traders!");

MinecraftServer.getGlobalEventHandler().addListener(PlayerChannelChatEvent.class,
        e -> System.out.println(e.chatChannel().name() + ": " + e.player() + " > " + e.message()));
```

Check the [javadocs](https://minestombrick.github.io/BrickChat/)
