// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.commands.commands;

import net.minecraft.client.gui.GuiScreen;
import net.augustus.commands.Command;

public class CommandInGameName extends Command
{
    public CommandInGameName() {
        super(".ign");
    }
    
    @Override
    public void commandAction(final String[] message) {
        if (CommandInGameName.mc.thePlayer != null) {
            final String name = CommandInGameName.mc.thePlayer.getName();
            GuiScreen.setClipboardString(name);
            this.sendChat("Your name: §c" + name);
        }
    }
    
    @Override
    public void helpMessage() {
        this.sendChat("§c.ign §7(Copys ingame name to clipboard)");
    }
}
