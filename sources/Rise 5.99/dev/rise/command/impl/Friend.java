package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;

@CommandInfo(name = "Friend", description = "Adds a player as a friend", syntax = ".friend <add/remove/list> <player>", aliases = {"friend", "friends", "f"})
public final class Friend extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        switch (args[0].toLowerCase()) {
            case "add": {
                final String player = args[1];

                if (!player.isEmpty()) {
                    for (final String name : Rise.INSTANCE.getFriends()) {
                        if (name.equalsIgnoreCase(player)) {
                            Rise.INSTANCE.getNotificationManager().registerNotification(String.format("\"%s\" is already a friend.", player));
                            return;
                        }
                    }

                    Rise.INSTANCE.getFriends().add(player);
                    Rise.INSTANCE.getNotificationManager().registerNotification(String.format("Added \"%s\" as a friend.", player));
                } else {
                    Rise.INSTANCE.getNotificationManager().registerNotification("Usage: " + commandInfo.syntax());
                }
                break;
            }

            case "remove": {
                final String player = args[1];

                if (!player.isEmpty()) {
                    for (final String name : Rise.INSTANCE.getFriends()) {
                        if (name.equalsIgnoreCase(player)) {
                            Rise.INSTANCE.getFriends().remove(player);
                            Rise.INSTANCE.getNotificationManager().registerNotification(String.format("Removed \"%s\" from the friends list.", player));
                            return;
                        }
                    }

                    Rise.INSTANCE.getNotificationManager().registerNotification("Couldn't find the player.");
                } else {
                    Rise.INSTANCE.getNotificationManager().registerNotification("Usage: " + commandInfo.syntax());
                }

                break;
            }

            case "list": {
                if (Rise.INSTANCE.getFriends().size() > 0) {
                    Rise.addChatMessage("All friended players:");

                    for (final String name : Rise.INSTANCE.getFriends()) {
                        Rise.addChatMessage(" - " + name);
                    }
                } else {
                    Rise.INSTANCE.getNotificationManager().registerNotification("No friended players found.");
                }
                break;
            }
        }
    }
}
