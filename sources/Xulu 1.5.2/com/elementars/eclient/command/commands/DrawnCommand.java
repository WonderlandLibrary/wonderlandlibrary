// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.module.Module;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.command.Command;

public class DrawnCommand extends Command
{
    public DrawnCommand() {
        super("drawn", "toggles if a module is drawn on array list", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Please specify which module you want drawn/undrawn");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        final Module m = Xulu.MODULE_MANAGER.getModuleByName(args[1]);
        if (m == null) {
            Command.sendChatMessage("Module not found.");
            return;
        }
        m.setDrawn(!m.isDrawn());
        Command.sendChatMessage(m.getDisplayName() + (m.isDrawn() ? " drawn" : " undrawn"));
    }
}
