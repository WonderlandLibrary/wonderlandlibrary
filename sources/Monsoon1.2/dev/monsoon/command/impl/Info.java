package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;

public class Info extends Command {
    public Info() {
        super("Info", "Shows info about the client.", ".info", "inf");
    }

    @Override
    public void onCommand(String[] args, String command) {
        Monsoon.sendMessage("Monsoon " + Monsoon.version);
        Monsoon.sendMessage("Modules:" + Monsoon.modules.size() + 1);
        Monsoon.sendMessage("Commands:" + Monsoon.commandManager.commands.size() + 1);
        Monsoon.sendMessage("Monsoon Username: " + Monsoon.monsoonUsername);
        Monsoon.sendMessage("Minecraft Username: " + mc.getSession().getUsername());
    }
}
