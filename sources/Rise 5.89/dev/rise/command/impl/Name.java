package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

@CommandInfo(name = "Name", description = "Copies your username to the clipboard", syntax = ".name", aliases = "name")
public final class Name extends Command {

    @Override
    public void onCommand(final String command, final String[] args) {
        final StringSelection stringSelection = new StringSelection(mc.thePlayer.getName());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
        Rise.INSTANCE.getNotificationManager().registerNotification("Copied your username to the clipboard.");
    }
}
