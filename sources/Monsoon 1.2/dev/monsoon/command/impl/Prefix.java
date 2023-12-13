package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;

public class Prefix extends Command {
    public Prefix() {
        super("Prefix", "set the command prefix", ".prefix <char>", "pre");
    }

    @Override
    public void onCommand(String[] args, String command) {
        if(args.length > 0) {
            Monsoon.commandManager.setPrefix(args[0]);
        }
    }
}
