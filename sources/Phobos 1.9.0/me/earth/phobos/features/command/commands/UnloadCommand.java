package me.earth.phobos.features.command.commands;

import me.earth.phobos.Phobos;
import me.earth.phobos.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Phobos.unload(true);
    }
}

