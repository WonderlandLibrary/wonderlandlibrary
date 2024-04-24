package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.command.Command;

public class UnloadCommand extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        claudius.unload(true);
    }
}

