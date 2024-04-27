package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.command.Command;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class UsernameCommand extends Command {
    public UsernameCommand() {
        super("username", "ign");
    }

    @Override
    public void execute(String[] args) {
        String username = mc.thePlayer.getName();

        chat("Username: " + username);

        StringSelection stringSelection = new StringSelection(username);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
    }
}
