package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.mod.command.Command;

import java.awt.*;
import java.net.URI;

public class YesCommand extends Command {
    public YesCommand() {
        super("YesCommand", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI("https://www.youtube.com/watch?v=tw0tinIDWzQ&ab_channel=JamesBandz-Topic"));
            } catch (Throwable ignored) {}
        }
    }
}

