// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.module.Module;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.command.Command;

public class ToggleCommand extends Command
{
    public ToggleCommand() {
        super("t", "Toggles modules", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        if (args.length <= 1) {
            Command.sendChatMessage("Not enough arguments!");
            return;
        }
        if (Xulu.MODULE_MANAGER.getModuleByName(args[1]) != null) {
            final Module module = Xulu.MODULE_MANAGER.getModuleByName(args[1]);
            module.toggle();
            Command.sendChatMessage(module.getName() + " toggled " + (module.isToggled() ? (Command.SECTIONSIGN() + "aON") : (Command.SECTIONSIGN() + "cOFF")));
        }
        else {
            Command.sendChatMessage("Module not found.");
        }
    }
}
