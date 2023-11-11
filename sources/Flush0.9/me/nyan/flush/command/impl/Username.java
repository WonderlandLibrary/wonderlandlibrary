package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.other.ChatUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class Username extends Command {
    public Username() {
        super("Username", "Copies the current Minecraft username to clipboard", "Username");
    }

    @Override
    public void onCommand(String[] args, String message) {
        StringSelection selection = new StringSelection(mc.thePlayer.getName());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        ChatUtils.println("§aCopied your username to clipboard: " + mc.thePlayer.getName());
    }
}
