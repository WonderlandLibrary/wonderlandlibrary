package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.client.IRC;

import java.io.IOException;

public class IRCCommand
        extends Command {
    public IRCCommand() {
        super("IRC");
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            IRCCommand.sendMessage(IRC.INSTANCE.status ? "\u00a7aIRC is connected." : "\u00a7cIRC is not connected.");
        } else if (commands.length == 2) {
            if (commands[0].equalsIgnoreCase("connect")) {
                IRCCommand.sendMessage("\u00a7aConnecting to the PhobosClient IRC...");
                try {
                    IRC.INSTANCE.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (commands[0].equalsIgnoreCase("disconnect")) {
                IRCCommand.sendMessage("\u00a7aDisconnecting from the PhobosClient IRC...");
                try {
                    IRC.INSTANCE.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (commands[0].equalsIgnoreCase("friendall")) {
                IRCCommand.sendMessage("\u00a7aFriending all...");
                try {
                    IRC.INSTANCE.friendAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (commands[0].equalsIgnoreCase("list")) {
                IRCCommand.sendMessage("\u00a7aListing PhobosClient Users...");
                try {
                    IRC.INSTANCE.list();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (commands.length >= 3) {
            if (commands[0].equalsIgnoreCase("say")) {
                IRCCommand.sendMessage("\u00a7aSending message to the PhobosClient chat server...");
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < commands.length - 1; ++i) {
                    builder.append(commands[i]).append(" ");
                }
                String message = builder.toString();
                try {
                    IRC.say(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (commands[0].equalsIgnoreCase("cockt")) {
                IRCCommand.sendMessage("\u00a7acockkk");
                try {
                    IRC.cockt(Integer.parseInt(commands[1]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

