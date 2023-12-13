package koks.command;

import koks.Koks;
import koks.api.registry.command.Command;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Command.Info(name = "reload", aliases = {"rl"}, description = "reload the client")
public class Reload extends Command {

    @Override
    public boolean execute(String[] args) {
        Koks.getKoks().onShutdown();
        Koks.getKoks().onStartup();
        sendMessage("§areloaded Client!");
        return true;
    }
}
