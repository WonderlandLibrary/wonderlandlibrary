package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.utils.misc.StringUtils;

public class ShortcutCommand extends Command {
    public ShortcutCommand() {
        super("shortcut");
    }

    @Override
    public void execute(final String[] args) {
        if (args.length <= 1) {
            chat("shortcut <add <shortcut_name> <script>/remove <shortcut_name>>");
            return;
        }

        String action = args[1].toLowerCase();

        if (action.equals("add") && args.length > 3) {
            try {
                LiquidSense.commandManager.registerShortcut(args[2], StringUtils.toCompleteString(args, 3));
                chat("Successfully added shortcut.");
            } catch (IllegalArgumentException e) {
                chat(e.getMessage());
            }
        } else if (action.equals("remove") && args.length >= 3) {
            if (LiquidSense.commandManager.unregisterShortcut(args[2])) {
                chat("Successfully removed shortcut.");
            } else {
                chat("Shortcut does not exist.");
            }
        } else {
            chat("shortcut <add <shortcut_name> <script>/remove <shortcut_name>>");
        }
    }
}