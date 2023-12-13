package me.earth.phobos.features.command.commands;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.command.Command;
import me.earth.phobos.features.modules.client.ClickGui;

public class PrefixCommand
        extends Command {
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("\u00a7cSpecify a new prefix.");
            return;
        }
        Phobos.moduleManager.getModuleByClass(ClickGui.class).prefix.setValue(commands[0]);
        Command.sendMessage("Prefix set to \u00a7a" + Phobos.commandManager.getPrefix());
    }
}

