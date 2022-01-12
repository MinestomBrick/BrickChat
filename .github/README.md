# BrickChat

An extension for [Minestom](https://github.com/Minestom/Minestom) to manage chat channels.

## Install

Get the latest jar file from [Github actions](https://github.com/MinestomBrick/BrickWorlds/actions) 
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

## Credits

* The [Minestom](https://github.com/Minestom/Minestom) project

## Contributing

Check our [contributing info](CONTRIBUTING.md)

