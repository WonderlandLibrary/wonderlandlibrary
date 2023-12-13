package me.earth.phobos.features.command.commands;

import me.earth.phobos.features.command.Command;
import me.earth.phobos.util.PlayerUtil;

import java.util.List;
import java.util.UUID;

public class HistoryCommand
        extends Command {
    public HistoryCommand() {
        super("history", new String[]{"<player>"});
    }

    @Override
    public void execute(String[] commands) {
        List<String> names;
        UUID uuid;
        if (commands.length == 1 || commands.length == 0) {
            HistoryCommand.sendMessage("\u00a7cPlease specify a player.");
        }
        try {
            uuid = PlayerUtil.getUUIDFromName(commands[0]);
        } catch (Exception e) {
            HistoryCommand.sendMessage("An error occured.");
            return;
        }
        try {
            names = PlayerUtil.getHistoryOfNames(uuid);
        } catch (Exception e) {
            HistoryCommand.sendMessage("An error occured.");
            return;
        }
        if (names != null) {
            HistoryCommand.sendMessage(commands[0] + "\u00c2\u00b4s name history:");
            for (String name : names) {
                HistoryCommand.sendMessage(name);
            }
        } else {
            HistoryCommand.sendMessage("No names found.");
        }
    }
}

