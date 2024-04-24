package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.client.Prefix;

public class PrefixCommand
        extends Command {
    public PrefixCommand() {
        super("prefix", new String[]{"<char>"});
    }

    @Override
    public void execute(String[] commands) {
        if (commands.length == 1) {
            Command.sendMessage("Current prefix is " + claudius.commandManager.getPrefix());
            return;
        }
        Prefix.INSTANCE.prefix.setValue(commands[0]);
        Command.sendMessage("Prefix changed to " + commands[0]);
        Prefix.INSTANCE.SetPrefix();
    }
}

