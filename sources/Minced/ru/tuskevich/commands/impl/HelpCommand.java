// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.Minced;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "help", description = "none")
public class HelpCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        for (final CommandAbstract command : Minced.getInstance().commandManager.getCommands()) {
            if (!(command instanceof HelpCommand)) {
                this.sendMessage(ChatFormatting.WHITE + "." + command.name + ChatFormatting.GRAY + " (" + ChatFormatting.WHITE + command.description + ChatFormatting.GRAY + ")");
            }
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.RED + "none");
    }
}
