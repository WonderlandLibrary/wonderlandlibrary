package dev.monsoon.command.impl;

import dev.monsoon.Monsoon;
import dev.monsoon.command.Command;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class IGN extends Command {
    public IGN() {
        super("IGN", "Copies your IGN to clipboard", ".ign", "username");
    }

    @Override
    public void onCommand(String[] args, String command) {
        StringSelection selection = new StringSelection(mc.getSession().getUsername());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        Monsoon.sendMessage("Coped your IGN to the clipboard! (" + mc.getSession().getUsername() + ")");
    }
}
