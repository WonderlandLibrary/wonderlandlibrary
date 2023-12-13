// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.module.Module;
import dev.xulu.settings.Value;
import com.elementars.eclient.command.SetBox;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.command.Command;

public class SetStringCommand extends Command
{
    public SetStringCommand() {
        super("setstring", "Sets a string easier", new String[0]);
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Please specify a module");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        if (args.length < 3) {
            Command.sendChatMessage("Please specify which setting you would like to change");
            return;
        }
        final Module m = Xulu.MODULE_MANAGER.getModuleByName(args[1]);
        if (m == null) {
            Command.sendChatMessage("Module not found!");
            return;
        }
        final Value s = Xulu.VALUE_MANAGER.getValueByMod(m, args[2]);
        if (s == null) {
            Command.sendChatMessage("Setting not found!");
            return;
        }
        if (s.getParentMod() != m) {
            Command.sendChatMessage(m.getDisplayName() + " has no setting " + s.getName());
            return;
        }
        if (s.isMode()) {
            SetBox.initTextBox(s);
        }
        else {
            Command.sendChatMessage(s.getName() + " is not a text setting!");
        }
    }
}
