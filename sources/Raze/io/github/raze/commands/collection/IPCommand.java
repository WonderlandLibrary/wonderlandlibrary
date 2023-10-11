package io.github.raze.commands.collection;

import io.github.raze.commands.system.Command;

public class IPCommand extends Command {

    public IPCommand() {
        super("IP", "Get the current server IP","ip", "i");
    }

    public String onCommand(String[] arguments, String command) {
        if(!mc.isSingleplayer())
            return String.format("You are playing on: %s", mc.getCurrentServerData().serverIP);
        else
            return "You are playing on a single-player world!";
    }

}
