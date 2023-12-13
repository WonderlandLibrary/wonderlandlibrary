package koks.command;

import koks.api.registry.command.Command;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Command.Info(name = "ingamename", aliases = {"name", "ign"}, description = "copy your ingame name")
public class IGN extends Command {

    @Override
    public boolean execute(String[] args) {
        GuiScreen.setClipboardString(getPlayer().getName());
        sendMessage("Name was saved in the clipboard!");
        return true;
    }
}
