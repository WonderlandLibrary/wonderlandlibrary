package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;

@CommandInfo(name = "Target", description = "Adds a player as a target", syntax = ".targets <add/remove/list> <player>", aliases = {"targets", "target"})
public final class Target extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        switch (args[0].toLowerCase()) {
            case "add": {
                final String player = args[1];

                if (!player.isEmpty()) {
                    for (final String name : Rise.INSTANCE.getTargets()) {
                        if (name.equalsIgnoreCase(player)) {
                            Rise.INSTANCE.getNotificationManager().registerNotification(String.format("\"%s\" is already a target.", player));
                            return;
                        }
                    }

                    Rise.INSTANCE.getTargets().add(player);
                    Rise.INSTANCE.getNotificationManager().registerNotification(String.format("Added \"%s\" as a target.", player));
                } else {
                    Rise.INSTANCE.getNotificationManager().registerNotification("Usage: " + commandInfo.syntax());
                }
                break;
            }

            case "remove": {
                final String player = args[1];

                if (!player.isEmpty()) {
                    for (final String name : Rise.INSTANCE.getTargets()) {
                        if (name.equalsIgnoreCase(player)) {
                            Rise.INSTANCE.getTargets().remove(player);
                            Rise.INSTANCE.getNotificationManager().registerNotification(String.format("Removed \"%s\" from the target list.", player));
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
                if (Rise.INSTANCE.getTargets().size() > 0) {
                    Rise.addChatMessage("All targeted players:");

                    for (final String name : Rise.INSTANCE.getTargets()) {
                        Rise.addChatMessage(" - " + name);
                    }
                } else {
                    Rise.INSTANCE.getNotificationManager().registerNotification("No targeted players found.");
                }
                break;
            }
        }
    }
}
