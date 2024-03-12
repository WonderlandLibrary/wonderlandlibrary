// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.commands.impl;

import dev.tenacity.utils.misc.IOUtils;
import dev.tenacity.commands.Command;

public class CopyNameCommand extends Command
{
    public CopyNameCommand() {
        super("name", "copies your name to the clipboard", ".name", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        IOUtils.copy(this.mc.thePlayer.getName());
        this.sendChatWithInfo("Copied your name to the clipboard");
    }
}
