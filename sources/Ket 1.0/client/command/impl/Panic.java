package client.command.impl;

import client.Client;
import client.command.Command;

public final class Panic extends Command {

    public Panic() {
        super("Turns off all modules", "panic", "p");
    }

    @Override
    public void execute(final String[] args) {
        Client.INSTANCE.getModuleManager().forEach(module -> module.setEnabled(false));
    }
}