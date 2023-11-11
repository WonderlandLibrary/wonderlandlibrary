package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.other.ChatUtils;

public class Help extends Command {
    public Help() {
        super("Help", "Shows help about commands.", "help", "h");
    }

    @Override
    public void onCommand(String[] args, String message) {
        ChatUtils.println("");
        ChatUtils.println("§9Commands: ");
        flush.getCommandManager().getCommands()
                .forEach(c -> ChatUtils.println("§9" + c.getName()
                        + (!c.getAlliases().isEmpty() ? "/" + String.join("/", c.getAlliases()) : "")
                        + ": §f" + c.getDescription()));
    }
}
