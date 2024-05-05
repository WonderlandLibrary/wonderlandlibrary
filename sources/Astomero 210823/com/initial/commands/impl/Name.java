package com.initial.commands.impl;

import com.initial.commands.*;
import net.minecraft.client.*;
import java.awt.*;
import net.minecraft.util.*;
import java.awt.datatransfer.*;

public class Name extends Command
{
    protected Minecraft mc;
    
    public Name() {
        super("Name", "Copies your username to clipboard", "name", new String[] { "name" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length == 1) {
            return;
        }
        final String name = this.mc.thePlayer.getName();
        final StringSelection stringSelection = new StringSelection(name);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        this.mc.thePlayer.addChatMessage(new ChatComponentText("§8> §aCopied your username to the clipboard!"));
    }
}
