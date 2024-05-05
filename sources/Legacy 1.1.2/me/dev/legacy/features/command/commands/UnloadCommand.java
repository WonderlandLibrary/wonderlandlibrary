package me.dev.legacy.features.command.commands;

import me.dev.legacy.Legacy;
import me.dev.legacy.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Legacy.unload(true);
    }
}

