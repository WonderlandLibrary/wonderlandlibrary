package me.dev.legacy.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.legacy.Legacy;
import me.dev.legacy.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : Legacy.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + Legacy.commandManager.getPrefix() + command.getName());
        }
    }
}

