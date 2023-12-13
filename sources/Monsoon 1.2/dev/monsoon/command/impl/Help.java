package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;

public class Help extends Command {
    public Help() {
        super("Help", "Need some help?", ".help", "hlp");
    }

    @Override
    public void onCommand(String[] args, String command) {
        for(Command c : Monsoon.commandManager.commands) {
            Monsoon.sendMessage(c.getName() + " / " + c.getDescription() + " / " + c.getSyntax());
        }
    }
}
